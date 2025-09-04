package com.devrobin.moneytracker.MVVM.MainViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devrobin.moneytracker.MVVM.Model.BudgetModel;
import com.devrobin.moneytracker.MVVM.Repository.BudgetRepository;

import java.util.List;

public class BudgetViewModel extends AndroidViewModel {

    private BudgetRepository budgetRepository;
    private LiveData<List<BudgetModel>> allBudgets;

    public BudgetViewModel(@NonNull Application application) {
        super(application);

        budgetRepository = new BudgetRepository(application);
        allBudgets = budgetRepository.getAllBudgets();
    }

    public LiveData<List<BudgetModel>> getAllBudgets() {
        return allBudgets;
    }

    public LiveData<BudgetModel> getBudgetById(int budgetId) {
        return budgetRepository.getBudgetById(budgetId);
    }

    public LiveData<List<BudgetModel>> getBudgetsByType(String budgetType) {
        return budgetRepository.getBudgetByType(budgetType);
    }


    // Budget Adding, Updating and Deleting Methods
    public void insertBudget(BudgetModel budgetModel) {
        budgetRepository.insertBudget(budgetModel);
    }

    public void updateBudget(BudgetModel budgetModel) {
        budgetRepository.updateBudget(budgetModel);
    }

    public void deleteBudget(BudgetModel budgetModel) {
        budgetRepository.deleteBudget(budgetModel);
    }

    // Additional methods for Spent Budget and Delete Budget
    public void updateBudgetSpent(int budgetId, double amount) {
        budgetRepository.updateBudgetSpent(budgetId, amount);
    }

    public void deleteAllBudgets() {
        budgetRepository.deleteAllBudgets();
    }


    // Additional methods for better budget management
    public LiveData<List<BudgetModel>> getBudgetsByMonth(int year, int month) {
        return budgetRepository.getBudgetByMonth(year, month);
    }

    public LiveData<List<BudgetModel>> getBudgetsByYear(int year) {
        return budgetRepository.getBudgetByYear(year);
    }

    public LiveData<Double> getTotalBudgetForMonth(int year, int month) {
        return budgetRepository.getTotalBudgetForMonth(year, month);
    }

    public LiveData<Double> getTotalSpentForMonth(int year, int month) {
        return budgetRepository.getTotalSpentForMonth(year, month);
    }

}
