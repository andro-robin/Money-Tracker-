package com.devrobin.moneytracker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.Model.CategoryChartData;
import com.devrobin.moneytracker.databinding.ItemPercentageRowBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyPermission;

public class PercentageRowAdapter extends RecyclerView.Adapter<PercentageRowAdapter.PercentageViewHolder> {

    private final List<CategoryChartData> items = new ArrayList<>();;
    private String currentType = "Expense";;
    private double total = 0d;

    public void submit(List<CategoryChartData> list, String type){

        items.clear();

        if (list != null){

            //Only add categories that have actual transactions
            for (CategoryChartData item : list){
                double value = "Income".equals(type) ? item.getIncome() : "Expense".equals(type) ? item.getExpense() : item.getTotal();

                if (value > 0){
                    items.add(item);
                }
            }

        }
        currentType = type != null ? type : "Expense";
        total = 0d;

        for (CategoryChartData d : items){
            total += "Income".equals(currentType) ? d.getIncome() : "Expense".equals(currentType) ? d.getExpense() : d.getTotal();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PercentageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemPercentageRowBinding percentageBinding = ItemPercentageRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PercentageViewHolder(percentageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PercentageViewHolder holder, int position) {

        CategoryChartData data = items.get(position);
        double value = "Income".equals(currentType) ? data.getIncome() : "Expense".equals(currentType) ? data.getExpense() : data.getTotal();
        double percent = total > 0 ? (value * 100.0) / total : 0;

        holder.percentageRowBinding.categoryName.setText(data.getCategory());
        holder.percentageRowBinding.progressBar.setProgress((int)Math.round(percent));
        holder.percentageRowBinding.percentage.setText(String.format("%.1f%%", percent));
        holder.percentageRowBinding.totalAmount.setText(String.format("%,.0f", value));

        //Set Progress Bar Color based on category type and percentage
        int color = getProgressBarColor(data.getCategory(), percent, currentType);
        holder.percentageRowBinding.progressBar.getProgressDrawable().setTint(color);


    }

    private int getProgressBarColor(String category, double percent, String type) {

        //For 100% completion, use special colors
        if (Math.abs(percent - 100.0) < 0.1){
            if ("Income".equals(type)){
                return Color.parseColor("#2196F3"); //Blue for Income at 100%
            }
            else {
                return Color.parseColor("#F44336"); // Red for Expense at 100%
            }
        }



        // For other percentages, use category-specific colors
        switch (category.toLowerCase()) {
            case "salary":
            case "job":
            case "business":
                return Color.parseColor("#4CAF50"); // Green for income categories
            case "food":
                return Color.parseColor("#FFD700"); // Yellow
            case "transportation":
                return Color.parseColor("#87CEEB"); // Light blue
            case "shopping":
                return Color.parseColor("#FFB6C1"); // Pink
            case "entertainment":
                return Color.parseColor("#98FB98"); // Light green
            case "health":
                return Color.parseColor("#FF6347"); // Tomato red
            case "education":
                return Color.parseColor("#9370DB"); // Medium purple
            case "rent":
            case "utilities":
                return Color.parseColor("#FFA500"); // Orange
            case "insurance":
                return Color.parseColor("#20B2AA"); // Light sea green
            default:
                // Default colors based on type
                if ("Income".equals(type)) {
                    return Color.parseColor("#4CAF50"); // Green for income
                } else {
                    return Color.parseColor("#F44336"); // Red for expense
                }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class PercentageViewHolder extends RecyclerView.ViewHolder{

        ItemPercentageRowBinding percentageRowBinding;

        public PercentageViewHolder(ItemPercentageRowBinding percentageRowBinding) {
            super(percentageRowBinding.getRoot());

            this.percentageRowBinding = percentageRowBinding;
        }
    }





}
