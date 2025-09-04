package com.devrobin.moneytracker.MVVM.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.devrobin.moneytracker.MVVM.DAO.BudgetDAO;
import com.devrobin.moneytracker.MVVM.Model.BudgetModel;
import com.devrobin.moneytracker.MVVM.TransactionDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetRepository {

    private BudgetDAO budgetDAO;
    private LiveData<List<BudgetModel>> allBudgets;


    public BudgetRepository(Application application){

        TransactionDatabase database = TransactionDatabase.getInstance(application);

        budgetDAO = database.budgetDAO();
        allBudgets = budgetDAO.getAllBudgets();
    }


    public LiveData<List<BudgetModel>> getAllBudgets(){
        return allBudgets;
    }

    public LiveData<BudgetModel> getBudgetById(int budgetId){
        return budgetDAO.getBudgetById(budgetId);
    }


    public LiveData<List<BudgetModel>> getBudgetByType(String budgetType){
        return budgetDAO.getBudgetsByType(budgetType);
    }


    // Budget Adding, Updating and Deleting Methods
    public void insertBudget(BudgetModel budgetModel) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> budgetDAO.insertBudget(budgetModel));
    }

    public void updateBudget(BudgetModel budgetModel) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> budgetDAO.updateBudget(budgetModel));
    }

    public void deleteBudget(BudgetModel budgetModel) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> budgetDAO.deleteBudget(budgetModel));
    }

    // Additional methods for Spent Budget and Delete Budget
    public void updateBudgetSpent(int budgetId, double amount) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> budgetDAO.updateBudgetSpent(budgetId, amount));
    }

    public void deleteAllBudgets() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> budgetDAO.deleteAllBudgets());
    }

    // Additional methods for better budget management
    public LiveData<List<BudgetModel>> getBudgetByMonth(int year, int month) {
        return budgetDAO.getBudgetsByMonth(year, month);
    }

    public LiveData<List<BudgetModel>> getBudgetByYear(int year) {
        return budgetDAO.getBudgetsByYear(year);
    }

    public LiveData<Double> getTotalBudgetForMonth(int year, int month) {
        return budgetDAO.getTotalBudgetForMonth(year, month);
    }

    public LiveData<Double> getTotalSpentForMonth(int year, int month) {
        return budgetDAO.getTotalSpentForMonth(year, month);
    }
}
