package com.devrobin.moneytracker.MVVM.MainViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.MVVM.Repository.TransRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.jvm.functions.Function1;
import utils.DailySummer;

public class TransViewModel extends AndroidViewModel {

    private TransRepository transRepository;

    private MutableLiveData<Date> selectedDate;
    private LiveData<List<TransactionModel>> transactionList;
    private LiveData<DailySummer> dailySummery;


    public TransViewModel(@NonNull Application application) {
        super(application);


        transRepository = new TransRepository(application);
        selectedDate = new MutableLiveData<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        selectedDate.setValue(calendar.getTime());

        transactionList = Transformations.switchMap(selectedDate, new Function1<Date, LiveData<List<TransactionModel>>>() {
            @Override
            public LiveData<List<TransactionModel>> invoke(Date date) {
                if (date != null){
                    return transRepository.getTransactionsByDate(date);
                }

                return new MutableLiveData<>(new ArrayList<>());
            }
        });

        dailySummery = Transformations.switchMap(selectedDate, new Function1<Date, LiveData<DailySummer>>() {
            @Override
            public LiveData<DailySummer> invoke(Date date) {
                if (date != null){
                    return transRepository.getDailySummer(date);
                }

                return new MutableLiveData<>(new DailySummer());
            }
        });


    }




    /// Get All Transaction
    public LiveData<List<TransactionModel>> getAllTransaction(){
        return transRepository.getAllTransaction();
    }

    //Get Transaction for selected Date
    public LiveData<List<TransactionModel>> getTransactionList(){

        return transactionList;
    }

    //get Daily Summery
    public LiveData<DailySummer> getDailySummery(){
        return dailySummery;
    }

    //Get Current Selected Date
    public LiveData<Date> getSelectedDate(){
        return selectedDate;
    }

    //Set Selected Date
    public void setSelectedDate(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        selectedDate.setValue(calendar.getTime());
    }

    //Navigation To Previous Date
    public void navigateToPreviousDate(){

        Date currentDate = selectedDate.getValue();

        if (currentDate != null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            setSelectedDate(calendar.getTime());
        }

    }

    //Navigate To Next Date
    public void navigateToNextDate(){
        Date currentDate = selectedDate.getValue();

        if (currentDate != null){

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            setSelectedDate(calendar.getTime());

        }
    }


    //addNew Transaction
    public void addNewTrans(TransactionModel transModel){
        transRepository.InsertTrans(transModel);
    }

    //update Transaction
    public void updateOldTrans(TransactionModel transModel){
        transRepository.UpdateTrans(transModel);
    }

    //Delete Transaction
    public void deleteOldTrans(TransactionModel transModel){
        transRepository.DeleteTrans(transModel);
    }
}
