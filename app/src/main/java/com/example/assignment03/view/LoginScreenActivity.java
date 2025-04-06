package com.example.assignment03.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.assignment03.R;
import com.example.assignment03.databinding.ActivityLoginScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// (a) Updating and adding xmls for my UI  and creating new Java Class Activities in package view
// (b) Setting up Authentication between my app and my project
// (c) Implementing this whole activity, - defining Sign In function, validating input before login in

public class LoginScreenActivity extends AppCompatActivity {

    ActivityLoginScreenBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.enterUserEmail.getText().toString().trim();
                String password = binding.enterUserPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    // if user leaves email textbox empty
                    Toast.makeText(LoginScreenActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();

                } else if (password.isEmpty()) {
                    // if user leaves password textbox empty
                    Toast.makeText(LoginScreenActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    // if user enters wrong pattern email in textbox
                    Toast.makeText(LoginScreenActivity.this, "Enter a valid email address", Toast.LENGTH_SHORT).show();

                }

                else {
                    // after checking  proceed to sign in
                    signIn(email, password);
                }



            }
        });


        binding.registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentObj = new Intent(getApplicationContext(), RegisterScreenActivity.class);
                startActivity(intentObj);

            }
        });

    }

    private void signIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(LoginScreenActivity.this,"Authentication passed ", Toast.LENGTH_SHORT ).show();

                            Intent intentObj = new Intent(getApplicationContext(), MovieSearchScreenActivity.class);
                            startActivity(intentObj);
                            finish();

                        }
                        else{

                            Toast.makeText(LoginScreenActivity.this,"Login attempt failed ", Toast.LENGTH_SHORT ).show();
                        }



                    }
                });

    }
}
