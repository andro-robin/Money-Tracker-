package com.devrobin.moneytracker.Views.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.FragmentTransactionChartBinding;

public class TransactionChart extends Fragment {

    public TransactionChart() {
        // Required empty public constructor
    }

    private FragmentTransactionChartBinding chartBinding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        chartBinding = FragmentTransactionChartBinding.inflate(inflater, container, false);
        return chartBinding.getRoot();
    }
}