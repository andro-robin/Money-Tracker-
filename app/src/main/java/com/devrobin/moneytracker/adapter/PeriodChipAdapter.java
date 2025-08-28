package com.devrobin.moneytracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.devrobin.moneytracker.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PeriodChipAdapter extends RecyclerView.Adapter<PeriodChipAdapter.PeriodChipViewHolder> {

    private final onChipClickListener chipClickListener;

    private final List<String> labels = new ArrayList<>();
    private int selectedPosition = -1;

    public PeriodChipAdapter(onChipClickListener chipClickListener) {
        this.chipClickListener = chipClickListener;
    }

    public void submit(List<String> newLabels){
        labels.clear();

        if (newLabels != null) labels.addAll(newLabels);;

        notifyDataSetChanged();
    }


    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        if (oldPosition >= 0 && oldPosition < labels.size()) {
            notifyItemChanged(oldPosition);
        }
        if (selectedPosition >= 0 && selectedPosition < labels.size()) {
            notifyItemChanged(selectedPosition);
        }
    }


    @NonNull
    @Override
    public PeriodChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip_text, parent, false);

        return new PeriodChipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeriodChipViewHolder holder, int position) {

        String label = labels.get(position);

        holder.textView.setText(label);

        // Set background and text color based on selection
        boolean isSelected = position == selectedPosition;
        holder.textView.setBackgroundResource(isSelected ? R.drawable.selected_chip_bg : R.drawable.spinner_background);
        holder.textView.setTextColor(holder.itemView.getContext().getColor(isSelected ? R.color.white : R.color.black));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipClickListener != null) {
                    setSelectedPosition(position);
                    chipClickListener.onClickChip(label);
                }
            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (chipClickListener != null){
//                    chipClickListener.onClickChip(label);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return labels.size();
    }


    class PeriodChipViewHolder extends RecyclerView.ViewHolder{

        final TextView textView;

        public PeriodChipViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView = itemView.findViewById(R.id.chipText);
        }
    }


    public interface onChipClickListener{

        void onClickChip(String label);

    }

}
