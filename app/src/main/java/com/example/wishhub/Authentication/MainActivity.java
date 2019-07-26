package com.example.wishhub.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishhub.HomePage.HomeActivity;
import com.example.wishhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Login Activity";
    private TextInputLayout texttEmail, texttPassword;
    private EditText ettEmail, ettPassword;
    private Button login, createAccount;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView forgetPassword;

    private BottomSheetBehavior sheetBehavior, sheetBehaviour2;
    FrameLayout frameLayout, frameLayout2;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.bottom_sheet_login);
        frameLayout2 = findViewById(R.id.bottom_sheet_register);

        sheetBehavior = BottomSheetBehavior.from(frameLayout);
        sheetBehaviour2 = BottomSheetBehavior.from(frameLayout2);

        view = findViewById(R.id.touch_outside);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch(i) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Toast.makeText(MainActivity.this, "Hidden", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        frameLayout2.animate().yBy(90).setDuration(700);
                        //sheetBehaviour2.setPeekHeight(0);
                        //Toast.makeText(MainActivity.this, "Expanded - " +sheetBehaviour2.getPeekHeight(), Toast.LENGTH_SHORT).show();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        frameLayout2.animate().yBy(-90).setDuration(700);
                        //sheetBehaviour2.setPeekHeight(187);
                        //Toast.makeText(MainActivity.this, "Collapsed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                //frameLayout2.animate().yBy(90).setDuration(500);
            }
        });

        login = findViewById(R.id.loginBtn);
        createAccount = findViewById(R.id.signUpBtn);

        mAuth = FirebaseAuth.getInstance();

        texttEmail = findViewById(R.id.TextViewsEmail);
        texttPassword = findViewById(R.id.TextViewsPassword);
        ettEmail = findViewById(R.id.etsEmail);
        ettPassword = findViewById(R.id.etsPassword);
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

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Register", Toast.LENGTH_SHORT).show();
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
