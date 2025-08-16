package com.devrobin.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.Model.CategoryModel;
import com.devrobin.moneytracker.MVVM.Model.TransactionModel;
import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.TransactionItemsBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Constant;
import utils.Helper;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context context;
    private ArrayList<TransactionModel> transList;

    public onTransItemClickListener transItemClickListener;


    public TransactionAdapter(Context context, ArrayList<TransactionModel> transList, onTransItemClickListener transItemClickListener) {
        this.context = context;
        this.transList = transList;
        this.transItemClickListener = transItemClickListener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TransactionItemsBinding itemsBinding = TransactionItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(itemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        TransactionModel transModel = transList.get(position);

        String transNote = transModel.getNote();

        if (transNote != null && !transNote.trim().isEmpty()){
            holder.itemsBinding.categoryNote.setText(transNote);
        }
        else {
            holder.itemsBinding.categoryNote.setText(transModel.getCategory());
        }

        holder.itemsBinding.categoryAmount.setText(String.valueOf(transModel.getAmount()));
//        holder.itemsBinding.date.setText(Helper.setDateFormate(transModel.getCurrentDate()));

        //Date Formatting
        Date transactionDate = transModel.getTransactionDate();
        if (transactionDate != null){

        }

        //Set Category Icon and Background
        CategoryModel transCategory = Constant.setCategoryDetails(transModel.getCategory());

        if (transCategory != null){
            holder.itemsBinding.categoryIcons.setImageResource(transCategory.getCategoryImg());
            holder.itemsBinding.categoryIcons.setBackgroundTintList(context.getColorStateList(R.color.blue));
        }
        else {
            holder.itemsBinding.categoryIcons.setImageResource(R.drawable.food);
            holder.itemsBinding.categoryIcons.setBackgroundTintList(context.getColorStateList(R.color.blue));
        }



        //Set Income and Expense Type
        if (transModel.getType().equals("Income")){
            holder.itemsBinding.categoryAmount.setTextColor(context.getColor(R.color.blue));
        }
        else if (transModel.getType().equals("Expense")){
            holder.itemsBinding.categoryAmount.setTextColor(context.getColor(R.color.red));
        }


    }

    @Override
    public int getItemCount() {
        return transList.size();
    }


    //ViewHolder
    public class TransactionViewHolder extends RecyclerView.ViewHolder{

        TransactionItemsBinding itemsBinding;

        public TransactionViewHolder(@NonNull TransactionItemsBinding itemsBinding) {
            super(itemsBinding.getRoot());

            this.itemsBinding = itemsBinding;

//            itemsBinding.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    int position = getAdapterPosition();
//
//                    if (transItemClickListener != null && position != RecyclerView.NO_POSITION){
//                        transItemClickListener.onTransItemClick(transList.get(position));
//                    }
//
//                }
//            });

        }
    }



    public interface onTransItemClickListener {

        void onTransItemClick(TransactionModel transactionModel);

    }

    public void setTransItemClickListener(onTransItemClickListener transItemClickListener) {
        this.transItemClickListener = transItemClickListener;
    }

    public void setTransList(ArrayList<TransactionModel> transList) {
        this.transList = transList;
        notifyDataSetChanged();
    }
}
