package com.devrobin.moneytracker.Views.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devrobin.moneytracker.R;
import com.devrobin.moneytracker.databinding.FragmentUserProfileBinding;


public class UserProfile extends Fragment {

    private FragmentUserProfileBinding profileBinding;

    public UserProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        return profileBinding.getRoot();

    }
}