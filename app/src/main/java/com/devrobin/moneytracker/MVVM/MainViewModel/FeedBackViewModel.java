package com.devrobin.moneytracker.MVVM.MainViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.devrobin.moneytracker.MVVM.Model.FeedbackModel;
import com.devrobin.moneytracker.MVVM.Repository.FeedbackRepository;

public class FeedBackViewModel extends AndroidViewModel {

    private final FeedbackRepository repository;

    public FeedBackViewModel(@NonNull Application application) {
        super(application);
        this.repository = new FeedbackRepository(application.getApplicationContext());
    }

    public long submitFeedback(int rating, String feedbackText, String contactInfo) {
        long timestamp = System.currentTimeMillis();
        FeedbackModel model = new FeedbackModel(rating, feedbackText, contactInfo, timestamp);
        return repository.insertFeedback(model);
    }
}
