package com.devrobin.moneytracker.MVVM.MainViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devrobin.moneytracker.MVVM.Model.AccountModel;
import com.devrobin.moneytracker.MVVM.Repository.AccountRepository;

import java.util.List;


public class AccountViewModel extends AndroidViewModel {

    private  AccountRepository accountRepository;
    private LiveData<List<AccountModel>> allAccounts;
    private LiveData<Double> totalBalance;

    public AccountViewModel(Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
        allAccounts = accountRepository.getAllAccounts();
        totalBalance = accountRepository.getTotalBalance();
    }

    public void insertAccount(AccountModel accountModel) {
        accountRepository.insertAccount(accountModel);
    }

    public void updateAccount(AccountModel accountModel) {
        accountRepository.updateAccount(accountModel);
    }

    public void deleteAccount(AccountModel accountModel) {
        accountRepository.deleteAccount(accountModel);
    }

    public LiveData<List<AccountModel>> getAllAccounts() {
        return allAccounts;
    }

    public LiveData<AccountModel> getAccountById(int accountId) {
        return accountRepository.getAccountById(accountId);
    }

    public LiveData<Double> getTotalBalance() {
        return totalBalance;
    }

    public void updateAccountBalance(int accountId, double amount) {
        accountRepository.updateAccountBalance(accountId, amount);
    }

    public void deleteAllAccounts() {
        accountRepository.deleteAllAccounts();
    }
}