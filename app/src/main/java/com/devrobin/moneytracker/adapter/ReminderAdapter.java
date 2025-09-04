package com.devrobin.moneytracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.MVVM.Model.ReminderModel;
import com.devrobin.moneytracker.databinding.ReminderListItemBinding;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context context;
    private List<ReminderModel> reminderList;
    private onReminderItemClickListener reminderItemClickListener;
    private onReminderLongClickListener reminderLongClickListener;

    public interface onReminderItemClickListener {
        void reminderItemClick(ReminderModel reminderModel);
    }

    public interface onReminderLongClickListener {
        void onReminderLongClick(ReminderModel reminderModel);
    }

    public ReminderAdapter(Context context, List<ReminderModel> reminderList, onReminderItemClickListener reminderItemClickListener) {
        this.context = context;
        this.reminderList = reminderList;
        this.reminderItemClickListener = reminderItemClickListener;
    }

    public void setReminderLongClickListener(onReminderLongClickListener reminderLongClickListener) {
        this.reminderLongClickListener = reminderLongClickListener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReminderListItemBinding binding = ReminderListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ReminderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderModel reminder = reminderList.get(position);
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public void updateList(List<ReminderModel> newList) {
        reminderList.clear();
        reminderList.addAll(newList);
        notifyDataSetChanged();
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {

        private ReminderListItemBinding binding;

        public ReminderViewHolder(@NonNull ReminderListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ReminderModel reminder) {
            binding.tvReminderName.setText(reminder.getReminderName());
            binding.tvReminderDetails.setText(reminder.getFrequency() + " • " + reminder.getFormattedTime());

            // Set click listeners
            binding.getRoot().setOnClickListener(v -> {
                if (reminderItemClickListener != null) {
                    reminderItemClickListener.reminderItemClick(reminder);
                }
            });

            binding.getRoot().setOnLongClickListener(v -> {
                if (reminderLongClickListener != null) {
                    reminderLongClickListener.onReminderLongClick(reminder);
                    return true;
                }
                return false;
            });
        }
    }
}
