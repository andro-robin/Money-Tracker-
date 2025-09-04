package com.devrobin.moneytracker.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.devrobin.moneytracker.MVVM.MainViewModel.TransViewModel;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.adapter.TransactionAdapter;
import com.devrobin.moneytracker.databinding.ActivityMainBinding;
import com.devrobin.moneytracker.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import utils.Constant;
import utils.DailySummer;
import utils.MonthlySummary;
import utils.SharedPrefsManager;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding homeBinding;


    private ListView srchClndrList;
    private ArrayAdapter<String> searchAdapter;
    private Toolbar toolbar;

    public TransViewModel transViewModel;
    private TransactionAdapter transAdapter;

    private TransactionAdapter.onTransItemClickListener transItemClickListener;

    private ArrayList<TransactionModel> transModelList;




    private long transId;
    private int Edit_Trans_RequestCode = 1;


    // Calendar & Date
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
    private SimpleDateFormat dayDateFormate = new SimpleDateFormat("EEE", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance();

    private String selectedDate;
    private SharedPrefsManager prefsManager;
    private String defaultCurrency;





    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        transViewModel = new ViewModelProvider(requireActivity()).get(TransViewModel.class);

        Constant.setCategories();



//
//        srchClndrList = findViewById(R.id.search_calendar);
        searchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        homeBinding.searchCalendar.setAdapter(searchAdapter);

        //ViewModel & and RecycleView
        loadTransViewModel();

        //ViewModel for DateBase Transaction
        setUpDateForTransactions();



        /// Calendar Handling By clicking right and left Button
        homeBinding.navigatePreviousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transViewModel.navigateToPreviousDate();
            }
        });

        homeBinding.navigateNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transViewModel.navigateToNextDate();
            }
        });

        homeBinding.currentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });




        return homeBinding.getRoot();
    }



    // ViewModel for RecycleView
    private void loadTransViewModel() {

        transViewModel.getTransactionList().observe(getViewLifecycleOwner(), new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(List<TransactionModel> transactionModels) {

                transModelList = (ArrayList<TransactionModel>) transactionModels;
                loadRecycleView();

            }
        });

    }

    //Load Transaction in RecycleView
    private void loadRecycleView() {

        transAdapter = new TransactionAdapter(getContext(), new ArrayList<>(), transItemClickListener);

        homeBinding.recycleViewList.setLayoutManager(new LinearLayoutManager(getContext()));
        homeBinding.recycleViewList.setHasFixedSize(true);
        homeBinding.recycleViewList.setAdapter(transAdapter);
        transAdapter.setTransList(transModelList);



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                TransactionModel transModel = transModelList.get(viewHolder.getAdapterPosition());

                transViewModel.deleteOldTrans(transModel);
            }
        }).attachToRecyclerView(homeBinding.recycleViewList);

    }

    //ViewModel For DateBase Transactions setUp
    public void setUpDateForTransactions(){

        //Observe Selected Date Changes
        transViewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                if (date != null){
                    updateDateDisplay(date);
                }
            }
        });


        //Observe transaction
        transViewModel.getTransactionList().observe(getViewLifecycleOwner(), new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(List<TransactionModel> transactionModels) {

                //Check Here transactionModel dont null value
                if (transactionModels != null && !transactionModels.isEmpty()){

                    transAdapter.setTransList(transModelList);
                    homeBinding.recycleViewList.setVisibility(View.VISIBLE);
                    homeBinding.emptyViewLayout.setVisibility(View.GONE);

                }
                else {
                    homeBinding.recycleViewList.setVisibility(View.GONE);
                    homeBinding.emptyViewLayout.setVisibility(View.VISIBLE);
                }

            }
        });


        //Observe Daily Summery
        transViewModel.getDailySummery().observe(getViewLifecycleOwner(), new Observer<DailySummer>() {
            @Override
            public void onChanged(DailySummer dailySummer) {
                if (dailySummer != null){
                    updateDailySummery(dailySummer);
                }
            }
        });

        transViewModel.getMonthlySummary().observe(getViewLifecycleOwner(), new Observer<MonthlySummary>() {
            @Override
            public void onChanged(MonthlySummary monthlySummary) {
                if (monthlySummary != null){
                    updateMonthlySummary(monthlySummary);
                }
            }
        });


    }

    @SuppressLint("DefaultLocale")
    private void updateMonthlySummary(MonthlySummary monthlySummary) {

        homeBinding.monthlyIncomeAmount.setText(String.format("%.0f", monthlySummary.getMonthlyIncome()));
        homeBinding.monthlyExpenseAmount.setText(String.format("%.0f", monthlySummary.getMonthlyExpense()));
        homeBinding.monthlyBalanceAmount.setText(String.format("%.0f", monthlySummary.getMonthlyBalance()));


    }

    @SuppressLint("DefaultLocale")
    private void updateDailySummery(DailySummer dailySummer) {

        // Get current default currency
        String currentDefaultCurrency = prefsManager.getDefaultCurrency();

        // Convert amounts to default currency if needed
        double dailyIncome = dailySummer.getTotalIncome();
        double dailyExpense = dailySummer.getTotalExpense();

        // Format amounts with currency symbol
        String incomeText = formatAmountWithCurrency(dailyIncome, currentDefaultCurrency);
        String expenseText = formatAmountWithCurrency(dailyExpense, currentDefaultCurrency);



        homeBinding.dailyIncomeAmount.setText(String.format(incomeText));
        homeBinding.dailyExpenseAmount.setText(String.format(expenseText));

        homeBinding.totalTransaction.setText(String.format("%d", dailySummer.getTransactionCount()));

        //  homeBinding.dailyIncomeAmount.setText(String.format("%.0f", dailySummer.getTotalIncome()));
        //    homeBinding.dailyExpenseAmount.setText(String.format("%.0f", dailySummer.getTotalExpense()));

        // homeBinding.totalTransaction.setText(String.format("%d", dailySummer.getTransactionCount()));

        // Update monthly balance display
        updateMonthlyBalanceDisplay();
    }


    private void updateMonthlyBalanceDisplay() {
        // Get current default currency
        String currentDefaultCurrency = prefsManager.getDefaultCurrency();

        // Get total balance from all accounts
        transViewModel.getAccountViewModel().getTotalBalance().observe(getViewLifecycleOwner(), totalBalance -> {
            if (totalBalance != null) {
                // Convert total balance to default currency if needed
                double convertedBalance = totalBalance; // This will be in the default currency

                // Format and display
                String balanceText = formatAmountWithCurrency(convertedBalance, currentDefaultCurrency);
                homeBinding.monthlyBalanceAmount.setText(balanceText);
            }
        });

        // For now, we'll use placeholder values for monthly income/expense
        // You can implement proper monthly calculation methods in your ViewModel later
        homeBinding.monthlyIncomeAmount.setText(formatAmountWithCurrency(0.0, currentDefaultCurrency));
        homeBinding.monthlyExpenseAmount.setText(formatAmountWithCurrency(0.0, currentDefaultCurrency));
    }

    private void updateDateDisplay(Date date) {
        homeBinding.currentDate.setText(dateFormat.format(date));
        homeBinding.nameOfDay.setText(dayDateFormate.format(date));
    }


    //Calendar DatePicker
    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = transViewModel.getSelectedDate().getValue();
        if (currentDate != null){
            calendar.setTime(currentDate);
        }


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int day, int month, int year) {

                calendar.set(day, month, year);
                transViewModel.setSelectedDate(calendar.getTime());

            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_calendar_list, menu);

        //SearchView
        MenuItem searchViewItem = menu.findItem(R.id.search);
        MenuItem calenderView = menu.findItem(R.id.calendar);

        SearchView searchView = (SearchView) searchViewItem.getActionView();
        CalendarView calendarView = (CalendarView) calenderView.getActionView();

        if (searchView != null) {
            searchView.setQueryHint("Type here to search");

            // Change background
            searchView.setBackgroundResource(R.color.gray);

            // Try to get and customize EditText
            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            EditText searchEditText = searchView.findViewById(id);
            if (searchEditText != null) {
                searchEditText.setHintTextColor(Color.BLACK);
                searchEditText.setTextColor(Color.BLACK);
            }

        }
//
        //CalendarView
        MenuItem clndrItem = menu.findItem(R.id.calendar);

        clndrItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
////                showDatePicker();
                return false;
            }
        });


        // Handle show/hide of ListView
        searchViewItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                srchClndrList.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                srchClndrList.setVisibility(View.GONE);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}