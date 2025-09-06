package utils.export;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.devrobin.moneytracker.MVVM.DAO.AccountDAO;
import com.devrobin.moneytracker.MVVM.DAO.BudgetDAO;
import com.devrobin.moneytracker.MVVM.DAO.TransactionDao;
import com.devrobin.moneytracker.MVVM.Model.AccountModel;
import com.devrobin.moneytracker.MVVM.Model.BudgetModel;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportUtils {
    private ExportUtils() {}

    public static long[] resolveRange(String range, String year, String month) {
        long now = System.currentTimeMillis();

        // Simple ranges; for custom range integrate a date picker in UI later.
        if ("Yearly".equalsIgnoreCase(range)) {
            return rangeForYear(Integer.parseInt(year));
        }
        else if ("Monthly".equalsIgnoreCase(range)) {
            return rangeForMonth(Integer.parseInt(year), Integer.parseInt(month));
        }
        else if ("All Time".equalsIgnoreCase(range)) {
            return new long[]{0L, Long.MAX_VALUE};
        }
        else {
            // Default: current month
            Date d = new Date(now);
            SimpleDateFormat yf = new SimpleDateFormat("yyyy", Locale.getDefault());
            SimpleDateFormat mf = new SimpleDateFormat("MM", Locale.getDefault());
            return rangeForMonth(Integer.parseInt(yf.format(d)), Integer.parseInt(mf.format(d)));
        }
    }

    private static long[] rangeForYear(int y) {
        long start = toMillis(y, 1, 1);
        long end = toMillis(y, 12, 31) + 86399999L;
        return new long[]{start, end};
    }

    private static long[] rangeForMonth(int y, int m) {
        long start = toMillis(y, m, 1);
        int lastDay = 28;
        if (m==1||m==3||m==5||m==7||m==8||m==10||m==12) lastDay = 31; else if (m==4||m==6||m==9||m==11) lastDay = 30; else lastDay = isLeap(y) ? 29 : 28;
        long end = toMillis(y, m, lastDay) + 86399999L;
        return new long[]{start, end};
    }

    private static boolean isLeap(int y) { return (y%4==0 && y%100!=0) || (y%400==0); }

    private static long toMillis(int y, int m, int d) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(java.util.Calendar.YEAR, y);
        c.set(java.util.Calendar.MONTH, m-1);
        c.set(java.util.Calendar.DAY_OF_MONTH, d);
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static Uri export(Context ctx, String format, String scope, long start, long end,
                             TransactionDao tDao, AccountDAO aDao, BudgetDAO bDao) throws IOException {
        String filenameBase = "moneytracker_export_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String mime;
        String ext;
        String data;

        List<TransactionModel> transactions = new ArrayList<>();
        List<AccountModel> accounts = new ArrayList<>();
        List<BudgetModel> budgets = new ArrayList<>();

        if (scope.contains("Transactions") || scope.contains("All")) {
            transactions = tDao.getTransactionByDateRange(start, end);
        }
        // Always fetch accounts to resolve transaction currencies
        accounts = aDao.getAllAccountsSync();
        if (scope.contains("Budgets") || scope.contains("All")) {
            budgets = bDao.getAllBudgetsSync();
        }

        // Build accountId -> currency map
        java.util.HashMap<Integer, String> accountIdToCurrency = new java.util.HashMap<>();
        for (AccountModel am : accounts) accountIdToCurrency.put(am.getAccountId(), am.getCurrency());

        if ("CSV".equalsIgnoreCase(format)) {
            mime = "text/csv";
            ext = ".csv";
            data = toCsv(transactions, accounts, budgets, scope, accountIdToCurrency);
        }
        else if ("JSON".equalsIgnoreCase(format)) {
            mime = "application/json";
            ext = ".json";
            data = toJson(transactions, accounts, budgets, scope, accountIdToCurrency);
        }
        else {
            // Minimal PDF as text for now; can be replaced by proper PDF later.
            mime = "application/pdf";
            ext = ".pdf";
            data = toPrettyText(transactions, accounts, budgets, scope, accountIdToCurrency);
        }

        String filename = filenameBase + ext;
        return saveToDownloads(ctx, filename, mime, data.getBytes(StandardCharsets.UTF_8));
    }

    private static String toCsv(List<TransactionModel> t, List<AccountModel> a, List<BudgetModel> b, String scope, java.util.Map<Integer, String> accountCurrency) {
        StringBuilder sb = new StringBuilder();
        if (scope.contains("Transactions") || scope.contains("All")) {
            sb.append("Transactions\n");
            sb.append("id,accountId,type,amount,currency,date,category,note\n");
            for (TransactionModel x : t) {
                sb.append(x.getTransId()).append(',')
                        .append(x.getAccountId()).append(',')
                        .append(x.getType()).append(',')
                        .append(x.getAmount()).append(',')
                        .append(accountCurrency.getOrDefault(x.getAccountId(), "")).append(',')
                        .append(x.getTransactionDate()).append(',')
                        .append(safe(x.getCategory())).append(',')
                        .append(safe(x.getNote())).append('\n');
            }
            sb.append('\n');
        }
        if (scope.contains("Accounts") || scope.contains("All")) {
            sb.append("Accounts\n");
            sb.append("id,name,type,currency,balance\n");
            for (AccountModel x : a) {
                sb.append(x.getAccountId()).append(',')
                        .append(safe(x.getAccountName())).append(',')
                        .append(safe(x.getCardType())).append(',')
                        .append(x.getCurrency()).append(',')
                        .append(x.getBalance()).append('\n');
            }
            sb.append('\n');
        }
        if (scope.contains("Budgets") || scope.contains("All")) {
            sb.append("Budgets\n");
            sb.append("id,category,type,amount,spent,year,month,day\n");
            for (BudgetModel x : b) {
                sb.append(x.getBudgetId()).append(',')
                        .append(safe(x.getCategory())).append(',')
                        .append(safe(x.getBudgetType())).append(',')
                        .append(x.getBudgetAmount()).append(',')
                        .append(x.getSpentAmount()).append(',')
                        .append(x.getYear()).append(',')
                        .append(x.getMonth()).append(',')
                        .append(x.getDay()).append('\n');
            }
        }
        return sb.toString();
    }

    private static String toJson(List<TransactionModel> t, List<AccountModel> a, List<BudgetModel> b, String scope, java.util.Map<Integer, String> accountCurrency) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean firstSection = true;
        if (scope.contains("Transactions") || scope.contains("All")) {
            sb.append("\"transactions\":[");
            for (int i = 0; i < t.size(); i++) {
                TransactionModel x = t.get(i);
                if (i>0) sb.append(',');
                sb.append('{')
                        .append("\"id\":").append(x.getTransId()).append(',')
                        .append("\"accountId\":").append(x.getAccountId()).append(',')
                        .append("\"type\":\"").append(escape(x.getType())).append("\",")
                        .append("\"amount\":").append(x.getAmount()).append(',')
                        .append("\"currency\":\"").append(escape(accountCurrency.getOrDefault(x.getAccountId(), ""))).append("\",")
                        .append("\"date\":").append(x.getTransactionDate()).append(',')
                        .append("\"category\":\"").append(escape(x.getCategory())).append("\",")
                        .append("\"note\":\"").append(escape(x.getNote())).append("\"}");
            }
            sb.append(']');
            firstSection = false;
        }
        if (scope.contains("Accounts") || scope.contains("All")) {
            if (!firstSection) sb.append(',');
            sb.append("\"accounts\":[");
            for (int i = 0; i < a.size(); i++) {
                AccountModel x = a.get(i);
                if (i>0) sb.append(',');
                sb.append('{')
                        .append("\"id\":").append(x.getAccountId()).append(',')
                        .append("\"name\":\"").append(escape(x.getAccountName())).append("\",")
                        .append("\"type\":\"").append(escape(x.getCardType())).append("\",")
                        .append("\"currency\":\"").append(escape(x.getCurrency())).append("\",")
                        .append("\"balance\":").append(x.getBalance()).append('}');
            }
            sb.append(']');
            firstSection = false;
        }
        if (scope.contains("Budgets") || scope.contains("All")) {
            if (!firstSection) sb.append(',');
            sb.append("\"budgets\":[");
            for (int i = 0; i < b.size(); i++) {
                BudgetModel x = b.get(i);
                if (i>0) sb.append(',');
                sb.append('{')
                        .append("\"id\":").append(x.getBudgetId()).append(',')
                        .append("\"category\":\"").append(escape(x.getCategory())).append("\",")
                        .append("\"type\":\"").append(escape(x.getBudgetType())).append("\",")
                        .append("\"amount\":").append(x.getBudgetAmount()).append(',')
                        .append("\"spent\":").append(x.getSpentAmount()).append(',')
                        .append("\"year\":").append(x.getYear()).append(',')
                        .append("\"month\":").append(x.getMonth()).append(',')
                        .append("\"day\":").append(x.getDay()).append('}');
            }
            sb.append(']');
        }
        sb.append('}');
        return sb.toString();
    }

    private static String toPrettyText(List<TransactionModel> t, List<AccountModel> a, List<BudgetModel> b, String scope, java.util.Map<Integer, String> accountCurrency) {
        StringBuilder sb = new StringBuilder();
        sb.append("MoneyTracker Export\n\n");
        if (scope.contains("Transactions") || scope.contains("All")) {
            sb.append("== Transactions ==\n");
            for (TransactionModel x : t) {
                sb.append("#").append(x.getTransId()).append(" ")
                        .append(x.getType()).append(" ")
                        .append(x.getAmount()).append(" ")
                        .append(accountCurrency.getOrDefault(x.getAccountId(), "")).append(" | ")
                        .append("date:").append(x.getTransactionDate()).append(" | cat:")
                        .append(safe(x.getCategory())).append("\n");
            }
            sb.append('\n');
        }
        if (scope.contains("Accounts") || scope.contains("All")) {
            sb.append("== Accounts ==\n");
            for (AccountModel x : a) {
                sb.append("#").append(x.getAccountId()).append(" ")
                        .append(x.getAccountName()).append(" (")
                        .append(x.getCardType()).append(") ")
                        .append(x.getCurrency()).append(" | bal:")
                        .append(x.getBalance()).append("\n");
            }
            sb.append('\n');
        }
        if (scope.contains("Budgets") || scope.contains("All")) {
            sb.append("== Budgets ==\n");
            for (BudgetModel x : b) {
                sb.append("#").append(x.getBudgetId()).append(" ")
                        .append(x.getCategory()).append(" ")
                        .append(x.getBudgetType()).append(" amt:")
                        .append(x.getBudgetAmount()).append(" spent:")
                        .append(x.getSpentAmount()).append(" date:")
                        .append(x.getYear()).append("-")
                        .append(x.getMonth()).append("-")
                        .append(x.getDay()).append("\n");
            }
        }
        return sb.toString();
    }

    private static String safe(String s) { return s == null ? "" : s.replace(","," "); }
    private static String escape(String s) { return s == null ? "" : s.replace("\\","\\\\").replace("\"","\\\""); }

    private static Uri saveToDownloads(Context ctx, String filename, String mime, byte[] bytes) throws IOException {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, filename);
        values.put(MediaStore.Downloads.MIME_TYPE, mime);
        values.put(MediaStore.Downloads.IS_PENDING, 1);
        ContentResolver resolver = ctx.getContentResolver();
        Uri collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        Uri item = resolver.insert(collection, values);
        if (item == null) throw new IOException("Cannot create file");
        try (OutputStream os = resolver.openOutputStream(item)) {
            if (os == null) throw new IOException("No stream");
            os.write(bytes);
            os.flush();
        }
        values.clear();
        values.put(MediaStore.Downloads.IS_PENDING, 0);
        resolver.update(item, values, null, null);
        return item;
    }

    public static void share(Context ctx, Uri file, String format) {
        String mime = "application/octet-stream";
        if ("CSV".equalsIgnoreCase(format)) mime = "text/csv";
        else if ("JSON".equalsIgnoreCase(format)) mime = "application/json";
        else if ("PDF".equalsIgnoreCase(format)) mime = "application/pdf";

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(mime);
        share.putExtra(Intent.EXTRA_STREAM, file);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ctx.startActivity(Intent.createChooser(share, "Share export"));
    }
}

