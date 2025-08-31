package com.devrobin.moneytracker.Views.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devrobin.moneytracker.MVVM.MainViewModel.ChartViewModel;
import com.devrobin.moneytracker.MVVM.Model.CategoryChartData;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.adapter.ChartPagerAdapter;
import com.devrobin.moneytracker.adapter.PercentageRowAdapter;
import com.devrobin.moneytracker.adapter.PeriodChipAdapter;
import com.devrobin.moneytracker.databinding.FragmentTransactionChartBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import utils.DailyChartData;
import utils.MonthlyChartData;
import utils.YearlyChartData;

public class TransactionChart extends Fragment {


    private FragmentTransactionChartBinding chartBinding;
    private Spinner spinnerTimeRange, spinnerDataType;
    private ChartViewModel chartViewModel;
    private PercentageRowAdapter percentageRowAdapter;
    private PeriodChipAdapter chipAdapter;
    private ChartPagerAdapter chartPagerAdapter;

    private String[] timeRangeOptions = {"Monthly", "Yearly"};

    public TransactionChart() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        chartBinding = FragmentTransactionChartBinding.inflate(inflater, container, false);




        AppCompatActivity compatActivity = (AppCompatActivity) getActivity();
        compatActivity.setSupportActionBar(chartBinding.mainToolbar);
        if (compatActivity.getSupportActionBar() != null){
            compatActivity.getSupportActionBar().setTitle("Analytics");
        }

        chartViewModel = new ViewModelProvider(this).get(ChartViewModel.class);

        setUpToggle();
        setUpSpinner();
        setUpList();
        setUpPeriodChips();
        setupChartPager();
        observeData();

        return chartBinding.getRoot();
    }

    private void setUpToggle() {

        setToggleSelected(chartBinding.expenseBtn, true);
        setToggleSelected(chartBinding.incomeBtn, false);
        chartViewModel.setDataType("Expense");


        View.OnClickListener listener =  new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.incomeBtn){

                    setToggleSelected(chartBinding.incomeBtn, true);
                    setToggleSelected(chartBinding.expenseBtn, false);
                    chartViewModel.setDataType("Income");

                }
                else {
                    setToggleSelected(chartBinding.expenseBtn, true);
                    setToggleSelected(chartBinding.incomeBtn, false);
                    chartViewModel.setDataType("Expense");

                }

            }


        };

        chartBinding.incomeBtn.setOnClickListener(listener);
        chartBinding.expenseBtn.setOnClickListener(listener);

    }

    private void setToggleSelected(TextView incomeBtn, boolean selected) {

        incomeBtn.setBackgroundResource(selected ? R.drawable.defaultbtn_bg : R.drawable.transparentbg);
        incomeBtn.setTextColor(getActivity().getColor(selected ? R.color.black : R.color.white));
    }


    private void setUpSpinner() {


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, timeRangeOptions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chartBinding.periodSpinner.setAdapter(arrayAdapter);
        chartBinding.periodSpinner.setSelection(0);
        chartBinding.periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                String sel = timeRangeOptions[position];

                // Refresh Chips when period type changes
                refreshChips();

                // Update the selected date to trigger data refresh
                Calendar calendar = Calendar.getInstance();
                chartViewModel.setSelectedDate(calendar.getTime());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void setUpList() {

        percentageRowAdapter = new PercentageRowAdapter();
        chartBinding.percentageList.setLayoutManager(new LinearLayoutManager(getContext()));
        chartBinding.percentageList.setAdapter(percentageRowAdapter);
    }



    private void setUpPeriodChips() {

        chipAdapter = new PeriodChipAdapter(new PeriodChipAdapter.onChipClickListener() {
            @Override
            public void onClickChip(String label) {
                onPeriodChipClicked(label);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        layoutManager.setStackFromEnd(true);
        chartBinding.periodLists.setLayoutManager(layoutManager);
        chartBinding.periodLists.setAdapter(chipAdapter);

        // populate initial chips
        refreshChips();
    }


    private void refreshChips() {

        List<String> labels = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        String sel = (String) chartBinding.periodSpinner.getSelectedItem();

        if ("Yearly".equals(sel)) {
            int currentYear = now.get(Calendar.YEAR);
            for (int y = currentYear - 4; y <= currentYear; y++) {
                if (y == currentYear) {
                    labels.add("This Year");
                } else {
                    labels.add(String.valueOf(y));
                }
            }

        } else {
            Calendar cursor = (Calendar) now.clone();
            cursor.add(Calendar.MONTH, -4);

            for (int i = 0; i < 5; i++) {
                // Check if this is the current month
                if (cursor.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                        cursor.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                    labels.add("This Month");
                } else {
                    String label = android.text.format.DateFormat.format("MMM yyyy", cursor).toString();
                    labels.add(label);
                }
                cursor.add(Calendar.MONTH, 1);
            }
        }
        chipAdapter.submit(labels);

        // Set the current period (This Month/This Year) as selected by default
        if (!labels.isEmpty()) {
            // Find the index of "This Month" or "This Year"
            int currentIndex = -1;
            for (int i = 0; i < labels.size(); i++) {
                if (labels.get(i).equals("This Month") || labels.get(i).equals("This Year")) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex >= 0) {
                chipAdapter.setSelectedPosition(currentIndex);
            } else {
                // Fallback to last item if current period not found
                chipAdapter.setSelectedPosition(labels.size() - 1);
            }
        }
    }


    private void onPeriodChipClicked(String label) {

        Calendar cal = Calendar.getInstance();
        String sel = (String) chartBinding.periodSpinner.getSelectedItem();

        if ("Yearly".equals(sel)){

            if (!"This Year".equals(label)){
                try {
                    int y = Integer.parseInt(label);
                    cal.set(Calendar.YEAR, y);
                } catch (NumberFormatException ignored) {}
            }
        }
        else {
            if (!"This Month".equals(label)) {
                try {
                    java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("MMM yyyy");
                    cal.setTime(df.parse(label));
                } catch (Exception ignored) {}
            }
        }

        chartViewModel.setSelectedDate(cal.getTime());
    }


    private void setupChartPager() {

        try {
            chartPagerAdapter = new ChartPagerAdapter(chartViewModel);
            chartBinding.chartPager.setAdapter(chartPagerAdapter);

            //Ensure Viewpager2 is properly initialized before notifying adapter
            chartBinding.chartPager.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (chartPagerAdapter != null){
                            chartPagerAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.e("AnalyticsActivity", "Error notifying chart adapter: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.e("AnalyticsActivity", "Error setting up chart pager: " + e.getMessage());
        }


    }


    private void observeData() {
        // Observe category data changes
        chartViewModel.getCategoryChartData().observe(getViewLifecycleOwner(), new Observer<List<CategoryChartData>>() {
            @Override
            public void onChanged(List<CategoryChartData> categoryList) {

                if (categoryList == null){
                    categoryList = new ArrayList<>();
                }

                // Update charts immediately
                if (chartPagerAdapter != null){
                    try {
                        chartPagerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Update percentage list
                percentageRowAdapter.submit(categoryList, chartViewModel.getDataType().getValue());
            }
        });


        // Observe data type changes (Income/Expense toggle)
        chartViewModel.getDataType().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                // Update charts immediately when data type changes
                if (chartPagerAdapter != null){
                    try {
                        chartPagerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Update percentage list
                List<CategoryChartData> current = chartViewModel.getCategoryChartData().getValue();
                if (current != null){
                    percentageRowAdapter.submit(current, s);
                }
            }
        });


        // Observe monthly data changes
        chartViewModel.getMonthlyChartData().observe(getViewLifecycleOwner(), new Observer<List<MonthlyChartData>>() {
            @Override
            public void onChanged(List<MonthlyChartData> monthlyChartData) {
                // Update charts immediately when monthly data changes
                if (chartPagerAdapter != null){
                    try {
                        chartPagerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        // Observe selected date changes
        chartViewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<Date>() {
            @Override
            public void onChanged(Date date) {
                // Update charts immediately when date changes
                if (chartPagerAdapter != null){
                    try {
                        chartPagerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


}