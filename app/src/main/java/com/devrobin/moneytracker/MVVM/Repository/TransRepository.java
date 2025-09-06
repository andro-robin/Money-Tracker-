package com.devrobin.moneytracker.MVVM.Repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devrobin.moneytracker.MVVM.DAO.AccountDAO;
import com.devrobin.moneytracker.MVVM.Model.AccountModel;
import com.devrobin.moneytracker.MVVM.Model.CategoryChartData;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.MVVM.DAO.TransactionDao;
import com.devrobin.moneytracker.MVVM.TransactionDatabase;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import utils.CurrencyConverter;
import utils.DailyChartData;
import utils.DailySummer;
import utils.MonthlyChartData;
import utils.MonthlySummary;

public class TransRepository{

    private TransactionDao transDao;
    private AccountDAO accountDAO;



    private LiveData<List<TransactionModel>> allTransaction;

    public TransRepository(Application application){

        TransactionDatabase database = TransactionDatabase.getInstance(application);

        transDao = database.transDao();
        accountDAO = database.accountDAO();
        allTransaction = transDao.getAllTransaction();
    }


    public LiveData<List<TransactionModel>> getTransactionsByDate(Date date){

        return transDao.getTransactionByDate(date.getTime());
    }


    public LiveData<DailySummer> getDailySummer(Date date){
        return transDao.getDailySummery(date.getTime());
    }

    /**
     * Get daily summary with currency conversion to default currency
     */
    public LiveData<DailySummer> getDailySummerWithConversion(Date date, String defaultCurrency){
        MutableLiveData<DailySummer> result = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Get transactions for the date
                List<TransactionModel> transactions = transDao.getTransactionByDateSync(date.getTime());

                double totalIncome = 0.0;
                double totalExpense = 0.0;
                int transactionCount = transactions.size();

                for (TransactionModel transaction : transactions) {
                    double amount = transaction.getAmount();
                    String transactionCurrency = getTransactionCurrency(transaction.getAccountId());

                    // Convert to default currency if needed
                    if (!transactionCurrency.equals(defaultCurrency)) {
                        amount = CurrencyConverter.convert(amount, transactionCurrency, defaultCurrency);
                    }

                    if ("INCOME".equals(transaction.getType())) {
                        totalIncome += amount;
                    } else if ("EXPENSE".equals(transaction.getType())) {
                        totalExpense += amount;
                    }
                }

                DailySummer dailySummer = new DailySummer(totalIncome, totalExpense, transactionCount);
                result.postValue(dailySummer);

            } catch (Exception e) {
                // Fallback to original method if conversion fails
                result.postValue(transDao.getDailySummery(date.getTime()).getValue());
            }
        });

        return result;
    }

    /**
     * Get transaction currency from account
     */
    private String getTransactionCurrency(int accountId) {
        try {
            // Get account currency from database
            AccountModel account = accountDAO.getAccountByIdSync(accountId);
            return account != null ? account.getCurrency() : "BDT"; // Default to BDT
        } catch (Exception e) {
            return "BDT"; // Default fallback
        }
    }


    public LiveData<MonthlySummary> getMonthlySummer(Date date){
        return transDao.getMonthlySummary(date.getTime());
    }

    /**
     * Get monthly summary with currency conversion to default currency
     */
    public LiveData<MonthlySummary> getMonthlySummaryWithConversion(Date date, String defaultCurrency){
        MutableLiveData<MonthlySummary> result = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // Get transactions for the month
                List<TransactionModel> transactions = transDao.getTransactionByMonthSync(date.getTime());

                double monthlyIncome = 0.0;
                double monthlyExpense = 0.0;
                int monthlyTransaction = transactions.size();

                for (TransactionModel transaction : transactions) {
                    double amount = transaction.getAmount();
                    String transactionCurrency = getTransactionCurrency(transaction.getAccountId());

                    // Convert to default currency if needed
                    if (!transactionCurrency.equals(defaultCurrency)) {
                        amount = CurrencyConverter.convert(amount, transactionCurrency, defaultCurrency);
                    }

                    if ("INCOME".equals(transaction.getType())) {
                        monthlyIncome += amount;
                    } else if ("EXPENSE".equals(transaction.getType())) {
                        monthlyExpense += amount;
                    }
                }

                MonthlySummary monthlySummary = new MonthlySummary(monthlyIncome, monthlyExpense, monthlyTransaction);
                result.postValue(monthlySummary);

            } catch (Exception e) {
                // Fallback to original method if conversion fails
                result.postValue(transDao.getMonthlySummary(date.getTime()).getValue());
            }
        });

        return result;
    }

    public LiveData<List<TransactionModel>> getAllTransaction(){
        return transDao.getAllTransaction();
    }


    //Chart Methods
    public LiveData<List<CategoryChartData>> getCategoryChartData(Date date) {
        return transDao.getCategoryChartData(date.getTime());
    }

    public LiveData<List<DailyChartData>> getDailyChartData(Date date) {
        return transDao.getDailyChartData(date.getTime());
    }

    public LiveData<List<MonthlyChartData>> getMonthlyChartData(Date date) {
        return transDao.getMonthlyChartData(date.getTime());
    }





    //Insert Transaction
    public void InsertTrans(TransactionModel transModel){

        ExecutorService executors = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());

        executors.execute(new Runnable() {
            @Override
            public void run() {
                transDao.insertTransaction(transModel);
            }
        });

    }


    //Update Transaction
    public void UpdateTrans(TransactionModel transModel){

        ExecutorService executors = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());

        executors.execute(new Runnable() {
            @Override
            public void run() {
                transDao.updateTransaction(transModel);
            }
        });

    }


    //Delete Transaction
    public void DeleteTrans(TransactionModel transModel){

        ExecutorService executors = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.myLooper());

        executors.execute(new Runnable() {
            @Override
            public void run() {
                transDao.deleteTransaction(transModel);
            }
        });

    }

}
