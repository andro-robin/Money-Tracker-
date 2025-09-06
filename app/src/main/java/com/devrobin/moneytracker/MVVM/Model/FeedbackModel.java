package com.devrobin.moneytracker.MVVM.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feedback_table")
public class FeedbackModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int rating; // 1-5
    private String feedbackText; // optional
    private String contactInfo; // optional email/phone
    private long timestamp; // epoch millis


    public FeedbackModel(int rating, String feedbackText, String contactInfo, long timestamp) {
        this.rating = rating;
        this.feedbackText = feedbackText;
        this.contactInfo = contactInfo;
        this.timestamp = timestamp;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
