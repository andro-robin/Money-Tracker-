package com.devrobin.moneytracker.MVVM.MainViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.MVVM.Repository.TransRepository;

import java.util.Calendar;
import java.util.List;

public class TransViewModel extends AndroidViewModel {

    private TransRepository transRepository;

    private LiveData<List<TransactionModel>> transactionList;


    public TransViewModel(@NonNull Application application) {
        super(application);

        transRepository = new TransRepository(application);

    }


    public LiveData<List<TransactionModel>> getTransactionList(Calendar calendar){

        return transactionList = transRepository.getTransaction();
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
