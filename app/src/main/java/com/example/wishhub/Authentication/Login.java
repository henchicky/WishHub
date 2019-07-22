package com.example.wishhub.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishhub.HomePage.HomeActivity;
import com.example.wishhub.Miscellaneous.NavigationPageToBeDelete;
import com.example.wishhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login Activity";
    private TextInputLayout texttEmail, texttPassword;
    private EditText ettEmail, ettPassword;
    private Button login, createAccount;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        texttEmail = findViewById(R.id.TextViewsEmail);
        texttPassword = findViewById(R.id.TextViewsPassword);
        ettEmail = findViewById(R.id.etsEmail);
        ettPassword = findViewById(R.id.etsPassword);
        login = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        forgetPassword = findViewById(R.id.forgetPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.INVISIBLE);
                final String email = ettEmail.getText().toString().trim();
                final String password = ettPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    if (email.isEmpty()) {
                        texttEmail.setError("Email is required!");
                    } else {
                        texttEmail.setError(null);
                    }
                    if (password.isEmpty()) {
                        texttPassword.setError("Password is required!");
                    } else {
                        texttPassword.setError(null);
                    }
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    login.setVisibility(View.INVISIBLE);
                    texttEmail.setError(null);
                    texttPassword.setError(null);
                    signIn(email, password);
                }
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear everything before
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please try again - " + task.getException(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    login.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}