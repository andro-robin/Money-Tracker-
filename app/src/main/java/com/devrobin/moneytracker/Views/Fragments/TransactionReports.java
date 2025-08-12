package com.devrobin.moneytracker.Views.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.FragmentTransactionReportsBinding;

public class TransactionReports extends Fragment {

    private FragmentTransactionReportsBinding reportsBinding;

    public TransactionReports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        reportsBinding = FragmentTransactionReportsBinding.inflate(inflater, container, false);
        return reportsBinding.getRoot();
    }
}