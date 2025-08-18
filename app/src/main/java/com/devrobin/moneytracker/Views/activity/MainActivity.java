package com.devrobin.moneytracker.Views.activity;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

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

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;

    private ListView srchClndrList;
    private ArrayAdapter<String> searchAdapter;
    private Toolbar toolbar;

    public TransViewModel transViewModel;
    private TransactionAdapter transAdapter;

    private TransactionAdapter.onTransItemClickListener transItemClickListener;

    private ArrayList<TransactionModel> transModelList;


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

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this);
        binding.bottomNavigation.setSelectedItemId(R.id.home);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrameLayout, new HomeFragment());
        transaction.commit();


        //BottomSheetDialogFragment
        binding.addFloatBtn.setOnClickListener(v -> {
            new AddTransaction().show(getSupportFragmentManager(), null);
        });



    }


    //BottomView Menu Items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        FragmentTransaction transactionMenu = getSupportFragmentManager().beginTransaction();

        int itemId = menuItem.getItemId();

        if (itemId == R.id.home){
            transactionMenu.replace(R.id.contentFrameLayout, new HomeFragment());
            getSupportFragmentManager().popBackStack();


        }
        else if (itemId == R.id.charts){
            transactionMenu.replace(R.id.contentFrameLayout, new TransactionChart());
            transactionMenu.addToBackStack(null);

        }
        else if (itemId == R.id.reports){
            transactionMenu.replace(R.id.contentFrameLayout, new TransactionReports());
            transactionMenu.addToBackStack(null);
        }
        else if (itemId == R.id.profile){
            transactionMenu.replace(R.id.contentFrameLayout, new UserProfile());
            transactionMenu.addToBackStack(null);
        }

        transactionMenu.commit();
        return true;
    }
}
