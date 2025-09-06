package com.devrobin.moneytracker.MVVM.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devrobin.moneytracker.MVVM.Model.FeedbackModel;

import java.util.List;

@Dao
public interface FeedbackDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFeedback(FeedbackModel feedback);

    @Query("SELECT * FROM feedback_table ORDER BY timestamp DESC")
    List<FeedbackModel> getAllFeedback();

}
