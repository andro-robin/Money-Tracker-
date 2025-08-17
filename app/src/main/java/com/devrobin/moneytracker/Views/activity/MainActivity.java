package com.devrobin.moneytracker.Views.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.MainViewModel.TransViewModel;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.Views.Fragments.AddTransaction;
import com.devrobin.moneytracker.Views.Fragments.HomeFragment;
import com.devrobin.moneytracker.Views.Fragments.TransactionChart;
import com.devrobin.moneytracker.Views.Fragments.TransactionReports;
import com.devrobin.moneytracker.Views.Fragments.UserProfile;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.adapter.TransactionAdapter;
import com.devrobin.moneytracker.databinding.ActivityMainBinding;
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


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    private ListView srchClndrList;
    private ArrayAdapter<String> searchAdapter;
    private Toolbar toolbar;

    public TransViewModel transViewModel;
    private TransactionAdapter transAdapter;

    private TransactionAdapter.onTransItemClickListener transItemClickListener;

    private ArrayList<TransactionModel> transModelList;

    //Widgets
    private BottomNavigationView navigationView;
    private FloatingActionButton actionButton;


    //Fragments
    private TransactionChart transactionChart = new TransactionChart();
    private TransactionReports transactionReports = new TransactionReports();
    private UserProfile userProfile = new UserProfile();
    private HomeFragment homeFragment = new HomeFragment();
    private AddTransaction addTransaction = new AddTransaction();



    private long transId;
    private int Edit_Trans_RequestCode = 1;


    // Calendar & Date
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault());
    private SimpleDateFormat dayDateFormate = new SimpleDateFormat("EEE", Locale.getDefault());
    private Calendar calendar = Calendar.getInstance();

    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Important Component
        transViewModel = new ViewModelProvider(this).get(TransViewModel.class);

        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        Constant.setCategories();


        //Find Id
        actionButton = findViewById(R.id.addFloadBtn);
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.home);

//
        srchClndrList = findViewById(R.id.search_calendar);
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        srchClndrList.setAdapter(searchAdapter);


        //BottomSheetDialogFragment
        actionButton.setOnClickListener(v -> {
            new AddTransaction().show(getSupportFragmentManager(), null);
        });

        //ViewModel & and RecycleView
        loadTransViewModel();

        //ViewModel for DateBase Transaction
        setUpDateForTransactions();

        binding.navigatePreviousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transViewModel.navigateToPreviousDate();
            }
        });

        binding.navigateNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transViewModel.navigateToNextDate();
            }
        });

        binding.currentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });


    }


    // ViewModel for RecycleView
    private void loadTransViewModel() {

        transViewModel.getTransactionList().observe(this, new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(List<TransactionModel> transactionModels) {

                transModelList = (ArrayList<TransactionModel>) transactionModels;
                loadRecycleView();

            }
        });

    }

    //Load Transaction in RecycleView
    private void loadRecycleView() {

        transAdapter = new TransactionAdapter(this, new ArrayList<>(), transItemClickListener);

        binding.recycleViewList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recycleViewList.setHasFixedSize(true);
        binding.recycleViewList.setAdapter(transAdapter);
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
        }).attachToRecyclerView(binding.recycleViewList);

    }

    //ViewModel For DateBase Transactions setUp
    public void setUpDateForTransactions(){

        //Observe Selected Date Changes
        transViewModel.getSelectedDate().observe(this, new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                if (date != null){
                    updateDateDisplay(date);
                }
            }
        });


        //Observe transaction
        transViewModel.getTransactionList().observe(this, new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(List<TransactionModel> transactionModels) {

                //Check Here transactionModel dont null value
                if (transactionModels != null && !transactionModels.isEmpty()){

                    transAdapter.setTransList(transModelList);
                    binding.recycleViewList.setVisibility(View.VISIBLE);
                    binding.emptyViewLayout.setVisibility(View.GONE);

                }
                else {
                    binding.recycleViewList.setVisibility(View.GONE);
                    binding.emptyViewLayout.setVisibility(View.VISIBLE);
                }

            }
        });


        //Observe Daily Summery
        transViewModel.getDailySummery().observe(this, new Observer<DailySummer>() {
            @Override
            public void onChanged(DailySummer dailySummer) {
                if (dailySummer != null){
                    updateDailySummery(dailySummer);
                }
            }
        });

        transViewModel.getMonthlySummary().observe(this, new Observer<MonthlySummary>() {
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

//       if (monthlySummary != null){

//           double monthlyBalance = monthlySummary.getMonthlyIncome() - monthlySummary.getMonthlyExpense();

           binding.monthlyIncomeAmount.setText(String.format("%.0f", monthlySummary.getMonthlyIncome()));
           binding.monthlyExpenseAmount.setText(String.format("%.0f", monthlySummary.getMonthlyExpense()));
           binding.monthlyBalanceAmount.setText(String.format("%.0f", monthlySummary.getMonthlyBalance()));

//       }

    }

    @SuppressLint("DefaultLocale")
    private void updateDailySummery(DailySummer dailySummer) {

        binding.dailyIncomeAmount.setText(String.format("%.0f", dailySummer.getTotalIncome()));
        binding.dailyExpenseAmount.setText(String.format("%.0f", dailySummer.getTotalExpense()));


        binding.totalTransaction.setText(String.format("%d", dailySummer.getTransactionCount()));

    }

    private void updateDateDisplay(Date date) {
        binding.currentDate.setText(dateFormat.format(date));
        binding.nameOfDay.setText(dayDateFormate.format(date));
    }


    //BottomView Menu Items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int itemId = menuItem.getItemId();

        if (itemId == R.id.charts){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, transactionChart).commit();
            return true;
        }
        else if (itemId == R.id.reports){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, transactionReports).commit();
            return true;
        }
        else if (itemId == R.id.profile){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, userProfile).commit();
            return true;
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
        }


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_calendar_list, menu);
        MenuItem menuItem = menu.findItem(R.id.search);


        //SearchView & Calendar

        //SearchView
        SearchView searchView = (SearchView) menuItem.getActionView();

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

        //CalendarView
        MenuItem clndrItem = menu.findItem(R.id.calendar);

        clndrItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
//                showDatePicker();
                return false;
            }
        });


        // Handle show/hide of ListView
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
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

        return super.onCreateOptionsMenu(menu);


    }


    //Calendar DatePicker
    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();
        Date currentDate = transViewModel.getSelectedDate().getValue();
        if (currentDate != null){
            calendar.setTime(currentDate);
        }


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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

}
