package com.devrobin.moneytracker.Views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import utils.User;

import com.devrobin.moneytracker.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;


    //FireBase Authentication
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseUser currentUser;
    private CollectionReference collectionReference = db.collection("Users");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();


        binding.haveAcnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goLogIn = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(goLogIn);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null){

                }
                else {

                }

            }
        };

        binding.btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(binding.signName.getText().toString())
                    && !TextUtils.isEmpty(binding.signEmail.getText().toString())
                    && !TextUtils.isEmpty(binding.signPaswrd.getText().toString())){

                    String name = binding.signName.getText().toString().trim();
                    String email = binding.signEmail.getText().toString().trim();
                    String password = binding.signPaswrd.getText().toString().trim();

                    createAccountWithEmailandPass(name, email, password);

                }
                else {
                    Toast.makeText(RegisterActivity.this, "Please Fill Input", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void createAccountWithEmailandPass(String name, String email, String password) {

        if (!TextUtils.isEmpty(binding.signName.getText().toString())
            && !TextUtils.isEmpty(binding.signEmail.getText().toString())
                && !TextUtils.isEmpty(binding.signPaswrd.getText().toString())){

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserID = currentUser.getUid();

                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userUid", currentUserID);
                                userObj.put("username", name);
                                userObj.put("userEmail", email);

                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {

                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                if (Objects.requireNonNull(task.getResult().exists())){

                                                                    String name = task.getResult().getString("username");
                                                                    String email = task.getResult().getString("userEmail");


                                                                    User user = User.getInstance();
                                                                    user.setUserId(currentUserID);
                                                                    user.setUserName(name);
                                                                    user.setUserEmail(email);



                                                                    Intent goActivity = new Intent(RegisterActivity.this, MainActivity.class);
                                                                    goActivity.putExtra("username", name);
                                                                    goActivity.putExtra("userId", currentUserID);
                                                                    goActivity.putExtra("userEmail", email);

                                                                    startActivity(goActivity);
                                                                    finish();
                                                                }
                                                                else{

                                                                }

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(RegisterActivity.this, "Failed to retrive data", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Failed to save user"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });



                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Sign Up Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }
}