package com.devrobin.moneytracker.Views.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.devrobin.moneytracker.MVVM.MainViewModel.CategoryViewModel;
import com.devrobin.moneytracker.MVVM.MainViewModel.TransViewModel;
import com.devrobin.moneytracker.MVVM.Model.AccountModel;
import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.Views.activity.MainActivity;
import com.devrobin.moneytracker.adapter.AccountAdapter;
import com.devrobin.moneytracker.adapter.CategoryAdapter;
import com.devrobin.moneytracker.databinding.FragmentAddTransactionBinding;
import com.devrobin.moneytracker.databinding.ListItemDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import utils.Constant;


public class AddTransaction extends BottomSheetDialogFragment {

    //Widget
    private FragmentAddTransactionBinding addBinding;

    private CategoryAdapter categoryAdapter;
    private TransactionModel transModel;
    private CategoryViewModel categoryViewModel;
    private TransViewModel transViewModel;


    //User Id
    private String currentUserName;
    private String currentUserId;

    //FireBase Connection
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Date selectedDate;
//

    public AddTransaction() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addBinding = FragmentAddTransactionBinding.inflate(inflater, container, false);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null){

                }
                else {

                }

            }
        };

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        transViewModel = new ViewModelProvider(requireActivity()).get(TransViewModel.class);

        selectedDate = ((MainActivity)getActivity()).transViewModel.getSelectedDate().getValue();
        if (selectedDate == null) {
            selectedDate = new Date(); // fallback to current date
        }


//        Income & Expense Btn

        transModel = new TransactionModel();

        addBinding.expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBinding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.btnselector_bg));
                addBinding.expenseBtn.setTextColor(getContext().getColor(R.color.white));

                addBinding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.defaultbtn_bg));
                addBinding.incomeBtn.setTextColor(getContext().getColor(R.color.black));


                transModel.setType(Constant.EXPENSE);
            }
        });

        addBinding.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBinding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.btnselector_bg));
                addBinding.incomeBtn.setTextColor(getContext().getColor(R.color.white));

                addBinding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.defaultbtn_bg));
                addBinding.expenseBtn.setTextColor(getContext().getColor(R.color.black));

                transModel.setType(Constant.INCOME);
            }
        });


        //Select Date when adding transactions
        addBinding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePicker = new DatePickerDialog(getContext());

                datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                        Calendar calendar = Calendar.getInstance();

                        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        calendar.set(Calendar.MONTH, datePicker.getMonth());
                        calendar.set(Calendar.YEAR, datePicker.getYear());

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dateToshow = simpleDateFormat.format(calendar.getTime());


                        addBinding.date.setText(dateToshow);
                    }
                });

                datePicker.show();

            }
        });


        //Select category when adding Transactions
        addBinding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), new Observer<List<CategoryModel>>() {
                    @Override
                    public void onChanged(List<CategoryModel> categories) {

                        if (categories != null &&  !categories.isEmpty()){

                            ListItemDialogBinding listItemDialog = ListItemDialogBinding.inflate(inflater);
                            AlertDialog categoryAlertDialog = new AlertDialog.Builder(getContext()).create();
                            categoryAlertDialog.setView(listItemDialog.getRoot());

                            ArrayList<CategoryModel> categoryList = new ArrayList<>(categories);


                            categoryAdapter = new CategoryAdapter(getContext(), categoryList, new CategoryAdapter.onItemClickListener() {
                                @Override
                                public void onItemClick(CategoryModel category) {
                                    addBinding.category.setText(category.getCategoryName());
                                    transModel.setCategory(category.getCategoryName());
                                    categoryAlertDialog.dismiss();

                                }
                            });

                            listItemDialog.categoryItems.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            listItemDialog.categoryItems.setAdapter(categoryAdapter);

                            categoryAlertDialog.show();

                        }
                        else {
                            Toast.makeText(getContext(), "No categories available. Please add categories first.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            }
        });



        //Select Account when adding account
        addBinding.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListItemDialogBinding listItemsBinding = ListItemDialogBinding.inflate(inflater);
                AlertDialog accountAlertDialog = new AlertDialog.Builder(getContext()).create();
                accountAlertDialog.setView(listItemsBinding.getRoot());

                ArrayList<AccountModel> accountList = new ArrayList<>();
                accountList.add(new AccountModel("Cash",0));
                accountList.add(new AccountModel("Card",0));
                accountList.add(new AccountModel("DBBL",0));
                accountList.add(new AccountModel("Bkash",0));
                accountList.add(new AccountModel("Bank",0));

                AccountAdapter accountAdapter = new AccountAdapter(getContext(), accountList, new AccountAdapter.onAccountItemClickListener() {
                    @Override
                    public void accountItemClick(AccountModel accountModel) {

                        addBinding.account.setText(accountModel.getAccountName());
                        accountAlertDialog.dismiss();

                    }
                });

                listItemsBinding.categoryItems.setLayoutManager(new LinearLayoutManager(getContext()));
                listItemsBinding.categoryItems.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                listItemsBinding.categoryItems.setHasFixedSize(true);
                listItemsBinding.categoryItems.setAdapter(accountAdapter);

                accountAlertDialog.show();
            }
        });

        addBinding.saveTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amountStr = addBinding.amount.getText().toString().trim();
                String date = addBinding.date.toString();
                String note = addBinding.note.getText().toString().trim();
                String category = addBinding.category.getText().toString();

                //Validation inputs
                if (amountStr.isEmpty()){
                    addBinding.amount.setError("Amount is required");
                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                    if (amount <= 0){
                        addBinding.amount.setError("Amount must be greater than 0");
                        return;
                    }
                } catch (NumberFormatException e) {
                    addBinding.amount.setError("Invalid amount");
                    return;
                }


                //Get Transaction Type
                String type = "INCOME";
                if (transModel.getType().equals("Expense")){
                    type = "EXPENSE";
//                    transModel.setAmount(amount* -1);
                }
//                else {
//                    transModel.setAmount(amount);
//                }

                Date transactionDate = selectedDate != null ? selectedDate : new Date();

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.setTime(transactionDate);

                Calendar currentDate = Calendar.getInstance();
                selectedDate.set(Calendar.HOUR_OF_DAY, currentDate.get(Calendar.HOUR_OF_DAY));
                selectedDate.set(Calendar.MINUTE, currentDate.get(Calendar.MINUTE));
                selectedDate.set(Calendar.SECOND, currentDate.get(Calendar.SECOND));
                selectedDate.set(Calendar.MILLISECOND, currentDate.get(Calendar.MILLISECOND));

                //Create Transaction
                TransactionModel transactionModel = new TransactionModel(type, category, amount, note, selectedDate.getTime());

                ((MainActivity)getActivity()).transViewModel.addNewTrans(transactionModel);
                ((MainActivity)getActivity()).transViewModel.getTransactionList();
                dismiss();
            }
        });


        return addBinding.getRoot();
    }

    public void setSelectedDate(Date selectedDate){
        this.selectedDate = selectedDate;
    }
}