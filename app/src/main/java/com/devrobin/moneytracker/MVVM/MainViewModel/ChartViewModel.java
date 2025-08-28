package com.devrobin.moneytracker.MVVM.MainViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.devrobin.moneytracker.MVVM.Model.CategoryChartData;
import com.devrobin.moneytracker.MVVM.Repository.TransRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.jvm.functions.Function1;
import utils.DailyChartData;
import utils.MonthlyChartData;
import utils.YearlyChartData;

public class ChartViewModel extends AndroidViewModel {

    private TransRepository transRepository;
    private MutableLiveData<Date> selectedDate;
    private MutableLiveData<String> chartType;
    private MutableLiveData<String> dataType;

    private LiveData<List<CategoryChartData>> categoryChartData;
    private LiveData<List<DailyChartData>> dailyChartData;
    private LiveData<List<MonthlyChartData>> monthlyChartData;

    public ChartViewModel(@NonNull Application application) {
        super(application);

        transRepository = new TransRepository(application);
        selectedDate = new MutableLiveData<>();
        chartType = new MutableLiveData<>();
        dataType = new MutableLiveData<>();

        // Set default values
        Calendar calendar = Calendar.getInstance();
        selectedDate.setValue(calendar.getTime());
        chartType.setValue("Monthly");
        dataType.setValue("Both");

        // Initialize LiveData with transformations
        categoryChartData = Transformations.switchMap(selectedDate, new Function1<Date, LiveData<List<CategoryChartData>>>() {
            @Override
            public LiveData<List<CategoryChartData>> invoke(Date date) {
                if (date != null) {
                    return transRepository.getCategoryChartData(date);
                }
                return new MutableLiveData<>();
            }
        });

        dailyChartData = Transformations.switchMap(selectedDate, new Function1<Date, LiveData<List<DailyChartData>>>() {
            @Override
            public LiveData<List<DailyChartData>> invoke(Date date) {
                if (date != null) {
                    return transRepository.getDailyChartData(date);
                }
                return new MutableLiveData<>();
            }
        });

        monthlyChartData = Transformations.switchMap(selectedDate, new Function1<Date, LiveData<List<MonthlyChartData>>>() {
            @Override
            public LiveData<List<MonthlyChartData>> invoke(Date date) {
                if (date != null) {
                    return transRepository.getMonthlyChartData(date);
                }
                return new MutableLiveData<>();
            }
        });
    }

    // Getters
    public LiveData<List<CategoryChartData>> getCategoryChartData() {
        return categoryChartData;
    }

    public LiveData<List<DailyChartData>> getDailyChartData() {
        return dailyChartData;
    }

    public LiveData<List<MonthlyChartData>> getMonthlyChartData() {
        return monthlyChartData;
    }

    public LiveData<String> getChartType() {
        return chartType;
    }

    public LiveData<String> getDataType() {
        return dataType;
    }

    public LiveData<Date> getSelectedDate() {
        return selectedDate;
    }

    // Setters
    public void setChartType(String type) {
        chartType.setValue(type);
    }

    public void setDataType(String type) {
        dataType.setValue(type);
    }

    public void setSelectedDate(Date date) {
        selectedDate.setValue(date);
    }

}
