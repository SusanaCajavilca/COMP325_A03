package com.example.assignment03.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.assignment03.databinding.ActivityRegisterScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// (d) Working on RegisterScreenActivity - define Register User function, validate confirmed password before registering

public class RegisterScreenActivity extends AppCompatActivity {

    ActivityRegisterScreenBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.registerUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.regiEmail.getText().toString().trim();
                String password = binding.regiPassword.getText().toString().trim();
                String confirmPassword = binding.confirmRegiPassword.getText().toString().trim();


                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    // if user leaves either one or more than one textbox empty (there are 3)
                    Toast.makeText(RegisterScreenActivity.this, "Fill all the fields!", Toast.LENGTH_SHORT).show();
                }

                else if (!password.equals(confirmPassword)) {
                    // if user puts different passwords
                    Toast.makeText(RegisterScreenActivity.this, "Passwords have to match!", Toast.LENGTH_SHORT).show();

                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    // if user enters wrong pattern email in textbox
                    Toast.makeText(RegisterScreenActivity.this, "Enter a valid email address!", Toast.LENGTH_SHORT).show();

                }

                else{

                    registerUser(email, password);
                }


            }
        });


        binding.goBacktoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });




    }


    private void registerUser(String email, String password){

        mAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Log.d("tag","createdUserWithEmailandPassword:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterScreenActivity.this,"RegisterUser with ID "+ user.getUid(), Toast.LENGTH_SHORT ).show();

                            Intent intentObj = new Intent(getApplicationContext(), LoginScreenActivity.class);
                            startActivity(intentObj);

                        }
                        else{

                            Log.d("tag","createUserWithEmailandPassword:failure",task.getException());
                            Toast.makeText(RegisterScreenActivity.this,"Authentication failed ", Toast.LENGTH_SHORT ).show();

                        }

                    }
                });

    }
}