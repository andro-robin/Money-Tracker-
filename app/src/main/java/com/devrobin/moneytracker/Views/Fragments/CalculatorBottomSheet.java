package com.devrobin.moneytracker.Views.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devrobin.moneytracker.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CalculatorBottomSheet extends BottomSheetDialogFragment {

    public CalculatorBottomSheet() {
    }


    private EditText inputAmount;
    private EditText noteText;
    private GridLayout keypad;

    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.calculator_layout, container, false);


        inputAmount = view.findViewById(R.id.inputMoney);
        noteText = view.findViewById(R.id.inputNote);


        keypad = view.findViewById(R.id.calculatorGrid);

        for (int i = 0; i < keypad.getChildCount(); i++){

            View child = keypad.getChildAt(i);

            if (child instanceof Button){

                Button btnCal = (Button) child;

                btnCal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CalculatorHandler();
                    }
                });

            }

        }


        return super.onCreateView(inflater, container, savedInstanceState);

    }

    private void CalculatorHandler() {

        if (!TextUtils.isEmpty(inputAmount.getText().toString())
            && !TextUtils.isEmpty(noteText.getText().toString())){

            String amount = inputAmount.getText().toString().trim();
            String note = noteText.getText().toString().trim();

        }

    }
}
