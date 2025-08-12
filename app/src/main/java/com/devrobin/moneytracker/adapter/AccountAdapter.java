package com.devrobin.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.Model.AccountModel;
import com.devrobin.moneytracker.databinding.AccountListItemsBinding;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private Context context;
    private ArrayList<AccountModel> accountList;

    private onAccountItemClickListener itemClickListener;

    public AccountAdapter(Context context, ArrayList<AccountModel> accountList,onAccountItemClickListener itemClickListener) {
        this.context = context;
        this.accountList = accountList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountViewHolder(AccountListItemsBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {

        AccountModel accountModel = accountList.get(position);

        holder.itemsBinding.acntName.setText(accountModel.getAccountName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.accountItemClick(accountModel);
                holder.itemsBinding.acntName.setText(accountModel.getAccountName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder{

        AccountListItemsBinding itemsBinding;

        public AccountViewHolder(@NonNull AccountListItemsBinding itemsBinding) {
            super(itemsBinding.getRoot());

            this.itemsBinding = itemsBinding;

        }
    }

    public interface onAccountItemClickListener{

        void accountItemClick(AccountModel accountModel);
    }

    public void setAccountList(ArrayList<AccountModel> accountList) {
        this.accountList = accountList;
        notifyDataSetChanged();
    }
}
