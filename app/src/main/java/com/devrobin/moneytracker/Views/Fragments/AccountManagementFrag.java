package com.devrobin.moneytracker.Views.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devrobin.moneytracker.MVVM.MainViewModel.AccountViewModel;
import com.devrobin.moneytracker.MVVM.Model.AccountModel;
import com.devrobin.moneytracker.adapter.AccountAdapter;
import com.devrobin.moneytracker.databinding.DialogAddEditAccountBinding;
import com.devrobin.moneytracker.databinding.FragmentAccountManagementBinding;

import java.util.ArrayList;

public class AccountManagementFrag extends Fragment {

    private FragmentAccountManagementBinding accountBinding;
    private AccountViewModel accountViewModel;
    private AccountAdapter accountAdapter;
    private ArrayList<AccountModel> accountList;


    public AccountManagementFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        accountBinding = FragmentAccountManagementBinding.inflate(inflater, container, false);

        // Initialize ViewModel
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

        // Initialize RecyclerView
        accountList = new ArrayList<>();
        accountAdapter = new AccountAdapter(getContext(), accountList, new AccountAdapter.onAccountItemClickListener() {
            @Override
            public void accountItemClick(AccountModel accountModel) {
                // Handle account item click if needed
            }
        });

        // Set edit and delete click listeners
        accountAdapter.setEditClickListener(new AccountAdapter.onEditClickListener() {
            @Override
            public void onEditClick(AccountModel accountModel) {
                showEditAccountDialog(accountModel);
            }
        });

        accountAdapter.setDeleteClickListener(new AccountAdapter.onDeleteClickListener() {
            @Override
            public void onDeleteClick(AccountModel accountModel) {
                showDeleteConfirmationDialog(accountModel);
            }
        });

        accountBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        accountBinding.recyclerView.setAdapter(accountAdapter);

        // Setup back button
        accountBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  onBackPressed();
            }
        });

        // Setup add account button
        accountBinding.btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAccountDialog();
            }
        });

        // Observe accounts
        accountViewModel.getAllAccounts().observe(getViewLifecycleOwner(), accounts -> {
            accountList.clear();
            if (accounts != null) {
                accountList.addAll(accounts);
            }
            accountAdapter.notifyDataSetChanged();
        });


        return accountBinding.getRoot();
    }


    private void showAddAccountDialog() {
        DialogAddEditAccountBinding dialogBinding = DialogAddEditAccountBinding.inflate(getLayoutInflater());
        dialogBinding.dialogTitle.setText("Add New Account");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogBinding.getRoot());

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String accountName = dialogBinding.etAccountName.getText().toString().trim();
                String balanceStr = dialogBinding.etInitialBalance.getText().toString().trim();

                if (!accountName.isEmpty()) {
                    double initialBalance = 0.0;
                    try {
                        if (!balanceStr.isEmpty()) {
                            initialBalance = Double.parseDouble(balanceStr);
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid balance amount", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AccountModel newAccount = new AccountModel(accountName, initialBalance);
                    accountViewModel.insertAccount(newAccount);
                    Toast.makeText(getContext(), "Account added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please enter account name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showEditAccountDialog(AccountModel accountModel) {
        DialogAddEditAccountBinding dialogBinding = DialogAddEditAccountBinding.inflate(getLayoutInflater());
        dialogBinding.dialogTitle.setText("Edit Account");
        dialogBinding.etAccountName.setText(accountModel.getAccountName());
        dialogBinding.etInitialBalance.setText(String.valueOf(accountModel.getBalance()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogBinding.getRoot());

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String accountName = dialogBinding.etAccountName.getText().toString().trim();
                String balanceStr = dialogBinding.etInitialBalance.getText().toString().trim();

                if (!accountName.isEmpty()) {
                    double balance = accountModel.getBalance();
                    try {
                        if (!balanceStr.isEmpty()) {
                            balance = Double.parseDouble(balanceStr);
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid balance amount", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    accountModel.setAccountName(accountName);
                    accountModel.setBalance(balance);
                    accountViewModel.updateAccount(accountModel);
                    Toast.makeText(getContext(), "Account updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Please enter account name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmationDialog(AccountModel accountModel) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete " + accountModel.getAccountName() + "? This action cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accountViewModel.deleteAccount(accountModel);
                        Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();


    }


}



