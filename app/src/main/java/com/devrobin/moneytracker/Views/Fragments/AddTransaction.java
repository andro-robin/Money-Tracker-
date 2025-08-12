package com.devrobin.moneytracker.Views.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

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
import java.util.Objects;

import utils.Constant;


public class AddTransaction extends BottomSheetDialogFragment {

    //Widget
    private FragmentAddTransactionBinding addBinding;
    private RecyclerView recyclerView;
    private TextView expenseBtn, incomeBtn;
    private CalculatorBottomSheet bottomSheet;
    private FragmentManager fragmentManager;

    private CategoryAdapter categoryAdapter;
    private CategoryModel ctgryModel;
//    private ArrayList<CategoryModel> categoryList = new ArrayList<>();

    private TransactionModel transModel;


    //User Id
    private String currentUserName;
    private String currentUserId;

    //FireBase Connection
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    Calendar calendar;



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

                ListItemDialogBinding listItemDialog = ListItemDialogBinding.inflate(inflater);
                AlertDialog categoryAlertDialog = new AlertDialog.Builder(getContext()).create();
                categoryAlertDialog.setView(listItemDialog.getRoot());

//                ArrayList<CategoryModel> categoryList = new ArrayList<>();


                categoryAdapter = new CategoryAdapter(getContext(), Constant.categories, new CategoryAdapter.onItemClickListener() {
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
        });


        //Select Account when adding account
        addBinding.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListItemDialogBinding listItemsBinding = ListItemDialogBinding.inflate(inflater);
                AlertDialog accountAlertDialog = new AlertDialog.Builder(getContext()).create();
                accountAlertDialog.setView(listItemsBinding.getRoot());

                ArrayList<AccountModel> accountList = new ArrayList<>();
                accountList.add(new AccountModel(0, "Cash"));
                accountList.add(new AccountModel(0, "Card"));
                accountList.add(new AccountModel(0, "DBBL"));
                accountList.add(new AccountModel(0, "Bkash"));
                accountList.add(new AccountModel(0, "Bank"));

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

                double amount = Double.parseDouble(addBinding.amount.getText().toString());
                String note = addBinding.note.getText().toString();
                String category = addBinding.category.getText().toString();


                if (transModel.getType().equals("Expense")){
                    transModel.setAmount(amount* -1);
                }
                else {
                    transModel.setAmount(amount);
                }

                ((MainActivity)getActivity()).transViewModel.addNewTrans(transModel);
                ((MainActivity)getActivity()).transViewModel.getTransactionList(calendar);
                dismiss();
            }
        });


        return addBinding.getRoot();
    }
}