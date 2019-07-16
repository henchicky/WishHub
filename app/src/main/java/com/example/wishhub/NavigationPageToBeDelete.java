package com.example.wishhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wishhub.Authentication.Login;
import com.example.wishhub.ChatSystem.Chat;
import com.example.wishhub.HomePage.Home;
import com.example.wishhub.SplashScreen.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;

public class NavigationPageToBeDelete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_page_to_be_delete);

        Button chat = findViewById(R.id.chatBtn);
        Button logOut = findViewById(R.id.LogOutBtn);
        Button home = findViewById(R.id.homeBtn);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Chat.class));
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(NavigationPageToBeDelete.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });

    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
