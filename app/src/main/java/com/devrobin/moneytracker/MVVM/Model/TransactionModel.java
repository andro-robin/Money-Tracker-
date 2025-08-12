package com.devrobin.moneytracker.MVVM.Model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "transaction_table")
public class TransactionModel {

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "currentDate")
    public String currentDate;

    @PrimaryKey(autoGenerate = true)
    private long transId;

    @Ignore
    public TransactionModel() {
    }

    public TransactionModel(String type, String category, double amount, String note, String currentDate, long transId) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.note = note;
        this.currentDate = currentDate;
        this.transId = transId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public long getTransId() {
        return transId;
    }

    public void setTransId(long transId) {
        this.transId = transId;
    }
}
