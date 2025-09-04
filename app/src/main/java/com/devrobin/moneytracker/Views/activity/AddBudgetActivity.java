package com.devrobin.moneytracker.Views.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.devrobin.moneytracker.MVVM.MainViewModel.BudgetViewModel;
import com.devrobin.moneytracker.MVVM.Model.BudgetModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.ActivityAddBudgetBinding;

import java.util.Calendar;

public class AddBudgetActivity extends AppCompatActivity {

    private ActivityAddBudgetBinding addBudgetBinding;
    private BudgetViewModel budgetViewModel;
    private BudgetModel currentBudget;
    private boolean isEditMode = false;
    private String selectedCategory = "";
    private String selectedFrequency = "";

    private final String[] categories = {"Food", "Transport", "Entertainment", "Shopping", "Health", "Education", "Bills", "Other"};
    private final String[] frequencies = {"Daily", "Monthly", "Yearly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        addBudgetBinding = ActivityAddBudgetBinding.inflate(getLayoutInflater());
        setContentView(addBudgetBinding.getRoot());

        // Initialize ViewModel
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        // Check if editing existing budget
        int budgetId = getIntent().getIntExtra("budget_id", -1);
        if (budgetId != -1) {
            isEditMode = true;
            loadBudget(budgetId);
            addBudgetBinding.tvTitle.setText("Edit Budget");
            addBudgetBinding.btnDelete.setVisibility(View.VISIBLE);
        }

        setupViews();
        setupClickListeners();



    }

    private void setupViews() {
        // Set default values
        if (!isEditMode) {
            addBudgetBinding.tvSelectedCategory.setText("Select Category");
            addBudgetBinding.tvFrequency.setText("Select Frequency");
        }
    }

    private void setupClickListeners() {

        addBudgetBinding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addBudgetBinding.btnCategoryDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoryDialog();
            }
        });

        addBudgetBinding.btnFrequencyDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFrequencyDialog();
            }
        });

        addBudgetBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBudget();
            }
        });

        addBudgetBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBudget();
            }
        });
    }

    private void showCategoryDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Select Category");
        alertBuilder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                selectedCategory = categories[which];
                addBudgetBinding.tvSelectedCategory.setText(selectedCategory);
            }
        });

        alertBuilder.show();
    }

    private void showFrequencyDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Select Frequency");
        alertBuilder.setItems(frequencies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                selectedFrequency = frequencies[which];
                addBudgetBinding.tvFrequency.setText(selectedFrequency);
            }
        });
        alertBuilder.show();
    }

    private void loadBudget(int budgetId) {
        budgetViewModel.getBudgetById(budgetId).observe(this, budgetModel -> {
            if (budgetModel != null) {
                currentBudget = budgetModel;
                selectedCategory = budgetModel.getCategory();
                selectedFrequency = budgetModel.getBudgetType();

                addBudgetBinding.tvSelectedCategory.setText(selectedCategory);
                addBudgetBinding.tvFrequency.setText(selectedFrequency);
                addBudgetBinding.etAmount.setText(String.valueOf((int) budgetModel.getBudgetAmount()));

                if (budgetModel.getNote() != null && !budgetModel.getNote().isEmpty()) {
                    addBudgetBinding.etNote.setText(budgetModel.getNote());
                }
            }
        });
    }


    private void saveBudget() {
        String amountStr = addBudgetBinding.etAmount.getText().toString().trim();
        String note = addBudgetBinding.etNote.getText().toString().trim();

        // Validation
        if (selectedCategory.isEmpty() || "Select Category".equals(selectedCategory)) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedFrequency.isEmpty() || "Select Frequency".equals(selectedFrequency)) {
            Toast.makeText(this, "Please select a frequency", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountStr.isEmpty()) {
            addBudgetBinding.etAmount.setError("Please enter amount");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                addBudgetBinding.etAmount.setError("Amount must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            addBudgetBinding.etAmount.setError("Invalid amount");
            return;
        }

        // Get current date
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (isEditMode && currentBudget != null) {
            // Update existing budget
            currentBudget.setCategory(selectedCategory);
            currentBudget.setBudgetType(selectedFrequency);
            currentBudget.setBudgetAmount(amount);
            currentBudget.setNote(note);
            currentBudget.setMonth(currentMonth);
            currentBudget.setYear(currentYear);
            currentBudget.setDay(currentDay);

            budgetViewModel.updateBudget(currentBudget);
            Toast.makeText(this, "Budget updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Create new budget
            BudgetModel newBudget = new BudgetModel();
            newBudget.setCategory(selectedCategory);
            newBudget.setBudgetType(selectedFrequency);
            newBudget.setBudgetAmount(amount);
            newBudget.setSpentAmount(0);
            newBudget.setNote(note);
            newBudget.setMonth(currentMonth);
            newBudget.setYear(currentYear);
            newBudget.setDay(currentDay);

            budgetViewModel.insertBudget(newBudget);
            Toast.makeText(this, "Budget added successfully", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
    private void deleteBudget() {
        if (currentBudget != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Budget")
                    .setMessage("Are you sure you want to delete this budget?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        budgetViewModel.deleteBudget(currentBudget);
                        Toast.makeText(this, "Budget deleted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}