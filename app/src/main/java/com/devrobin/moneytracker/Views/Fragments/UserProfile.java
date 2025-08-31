package com.devrobin.moneytracker.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.devrobin.moneytracker.Views.activity.AccountManagement;
import com.devrobin.moneytracker.Views.activity.DeleteAllData;
import com.devrobin.moneytracker.databinding.FragmentUserProfileBinding;


public class UserProfile extends Fragment {

    private FragmentUserProfileBinding profileBinding;

    public UserProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileBinding = FragmentUserProfileBinding.inflate(inflater, container, false);


        setUpClickListener();

        return profileBinding.getRoot();
    }

    private void setUpClickListener() {

        profileBinding.ProfileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.accountContainerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), AccountManagement.class);
                startActivity(intent);

            }
        });

        profileBinding.categoryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();

            }
        });

        profileBinding.currencyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.notificationContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();

            }
        });

        profileBinding.reminderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();

            }
        });

        profileBinding.budgetContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();

            }
        });

        profileBinding.exportContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.PasswordContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.calendarContainerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.languageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.themesContainerr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.fontContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.deleteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DeleteAllData.class);
                startActivity(intent);
            }
        });

        profileBinding.securityContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.termsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.aboutUsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

        profileBinding.feedBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Click SuccessFull", Toast.LENGTH_SHORT).show();
            }
        });

    }
}