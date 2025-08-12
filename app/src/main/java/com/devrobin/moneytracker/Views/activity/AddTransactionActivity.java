package com.devrobin.moneytracker.Views.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.adapter.CategoryAdapter;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.databinding.ActivityAddTransactionViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionActivity extends AppCompatActivity {

    private ActivityAddTransactionViewBinding addBinding;

    //Models, Adapter and ArrayList
    private CategoryAdapter categoryAdapter;
    private CategoryModel ctgryModel;
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();
    private TransactionModel trnsnModel;

    //Widgets
    private RecyclerView recyclerView;
    private TextView expenseBtn, incomeBtn;

    //User Id
    private String currentUserName;
    private String currentUserId;

    //FireBase Connection
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    //Calculator input Store
    private StringBuilder calculatorInput =  new StringBuilder();
    // Variables for calculation
    private String currentOperator = "";
    private double firstNumber = 0;
    private boolean isOperatorSet = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        addBinding = ActivityAddTransactionViewBinding.inflate(getLayoutInflater());
        setContentView(addBinding.getRoot());

//
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//
//        if (User.getInstance() != null){
//
//            currentUserName = User.getInstance().getUserName();
//            currentUserId = User.getInstance().getUserId();
//
//
//        }

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


////        Income & Expense Btn

        addBinding.expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBinding.expenseBtn.setBackground(getDrawable(R.drawable.btnselector_bg));
                addBinding.expenseBtn.setTextColor(getColor(R.color.white));

                addBinding.incomeBtn.setBackground(getDrawable(R.drawable.defaultbtn_bg));
                addBinding.incomeBtn.setTextColor(getColor(R.color.black));

            }
        });

        addBinding.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBinding.incomeBtn.setBackground(getDrawable(R.drawable.btnselector_bg));
                addBinding.incomeBtn.setTextColor(getColor(R.color.white));

                addBinding.expenseBtn.setBackground(getDrawable(R.drawable.defaultbtn_bg));
                addBinding.expenseBtn.setTextColor(getColor(R.color.black));
            }
        });


//        Unvisible Calculator Containers
        addBinding.calculatorContainer.setVisibility(GONE);



//        RecycleVew & Adapters
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryList, new CategoryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(CategoryModel category) {

                addBinding.calculatorContainer.setVisibility(VISIBLE);



                String categoryName = category.getCategoryName();
                int categoryImg = category.getCategoryImg();

//                addTransaction(categoryName, categoryImg);

                setCalculatorButtons();

            }
        });

        addBinding.categoryView.setLayoutManager(new GridLayoutManager(this, 4));
        addBinding.categoryView.setHasFixedSize(true);

        addBinding.categoryView.setAdapter(categoryAdapter);

        categoryAdapter.setCategoryList(categoryList);

        categoryList.add(new CategoryModel("Food", R.drawable.food));
        categoryList.add(new CategoryModel("Food", R.drawable.food));
        categoryList.add(new CategoryModel("Food", R.drawable.food));
        categoryList.add(new CategoryModel("Food", R.drawable.food));
        categoryList.add(new CategoryModel("Food", R.drawable.food));

        addBinding.categoryView.setAdapter(categoryAdapter);



//        categoryAdapter.setListener(new CategoryAdapter.onItemClickListener() {
//            @Override
//            public void onItemClick(CategoryModel category) {
//
//                addBinding.calculatorContainer.setVisibility(VISIBLE);
//
//                String categoryName = category.getCategoryName();
//                int categoryImg = category.getCategoryImg();
//
//                addTransaction(categoryName, categoryImg);
//
//                setCalculatorButtons();
//            }
//        });

        //Calculator Buttons and Settings

//        Store result


    }

    //Handle Calculator Buttons click
    private void setCalculatorButtons() {

        View.OnClickListener numberClicks = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button v = (Button) view;
                String input = v.getText().toString();

                calculatorInput.append(input);
                addBinding.inputMoney.setText(calculatorInput.toString());

                if (isOperatorSet) {
                    calculateResult();
                }
            }
        };

        // Number buttons
        addBinding.btn0.setOnClickListener(numberClicks);
        addBinding.btn1.setOnClickListener(numberClicks);
        addBinding.btn2.setOnClickListener(numberClicks);
        addBinding.btn3.setOnClickListener(numberClicks);
        addBinding.btn4.setOnClickListener(numberClicks);
        addBinding.btn5.setOnClickListener(numberClicks);
        addBinding.btn6.setOnClickListener(numberClicks);
        addBinding.btn7.setOnClickListener(numberClicks);
        addBinding.btn8.setOnClickListener(numberClicks);
        addBinding.btn9.setOnClickListener(numberClicks);

        // Dot button
        addBinding.btnDot.setOnClickListener(v -> {
            if (!calculatorInput.toString().contains(".")) {
                calculatorInput.append(".");
                addBinding.inputMoney.setText(calculatorInput.toString());
            }
        });

        // Delete button
        addBinding.btnDelete.setOnClickListener(v -> {
            int length = calculatorInput.length();
            if (length > 0) {
                calculatorInput.deleteCharAt(length - 1);
                addBinding.inputMoney.setText(calculatorInput.toString());
            }
        });

        // Operator buttons
        addBinding.btnPlus.setOnClickListener(v -> {
            handleOperator("+");
            calculateResult();
        });
        addBinding.btnMinus.setOnClickListener(v -> {
            handleOperator("-");
            calculateResult();
        });
//        addBinding.btnmu.setOnClickListener(v -> handleOperator("*"));
//        addBinding.btnDivide.setOnClickListener(v -> handleOperator("/"));

        // Prevent keyboard for inputMoney
        addBinding.inputMoney.setShowSoftInputOnFocus(false);

        addBinding.inputMoney.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                addBinding.calculatorContainer.setVisibility(VISIBLE);
            } else {
                addBinding.calculatorContainer.setVisibility(GONE);
            }
        });

        // Show normal keyboard for note
        addBinding.inputNote.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                addBinding.calculatorContainer.setVisibility(GONE);
            }
        });

        // Calendar button
        addBinding.btnCalendar.setOnClickListener(v -> showDatePicker());
    }

    private void handleOperator(String operator) {

        if (calculatorInput.length() > 0 && !isOperatorSet){

            try {

                firstNumber = Double.parseDouble(calculatorInput.toString());
                calculatorInput.setLength(0);
                currentOperator = operator;
                isOperatorSet = true;

            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }

        }


    }


    //Calculate The Results
    private void calculateResult() {

        try {
            double secondNumber = Double.parseDouble(calculatorInput.toString());
            double result = 0;

            switch (currentOperator){
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0){
                        result = firstNumber / secondNumber;
                    }
                    else {
                        addBinding.inputMoney.setText("Error");
                        calculatorInput.length();
                    }
                    break;

            }

            //Update Input
            calculatorInput.length();
            calculatorInput.append(removeTrailingZero(result));
            addBinding.inputMoney.setText(calculatorInput.toString());

            //Reset
            isOperatorSet = false;
            currentOperator = "";

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }



    }

    private String removeTrailingZero(double number) {

        if (number == (long) number){
            return String.format("%d", (long) number);
        }
        else {
            return String.format("%s", number);
        }

    }

    //Date picker button Set up Calendar
    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int date, int month, int year) {

                String selectDate = date + "/" + (month + 1) + "/" + year;

            }
        }, year, month, day);

    }

//    private void addTransaction(String categoryName, int categoryImg) {
//
//        String amount = addBinding.inputMoney.getText().toString().trim();
//        String note = addBinding.inputNote.getText().toString().trim();
//
//
//        if (!TextUtils.isEmpty(addBinding.inputMoney.getText().toString())
//            && !TextUtils.isEmpty(addBinding.inputNote.getText().toString())
//            && !TextUtils.isEmpty(categoryName) && categoryImg != 0){
//
//            TransactionModel model = new TransactionModel(amount, note);
//
//            Intent resultIntent = new Intent(this, HomeFragment.class);
//            resultIntent.putExtra( "transaction", (Parcelable) model);
//            setResult(RESULT_OK, resultIntent);
//            finish();
//            startActivity(resultIntent);
//        }
//
//    }
}