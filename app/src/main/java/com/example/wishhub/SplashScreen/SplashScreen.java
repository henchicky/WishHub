package com.example.wishhub.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wishhub.Authentication.ForgetPassword;
import com.example.wishhub.Authentication.Login;
import com.example.wishhub.Authentication.RegisterActivity;
import com.example.wishhub.R;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    @SuppressLint("RestrictedApi")
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    Button enterLoginPage, enterRegisterPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        models = new ArrayList<>();
        models.add(new Model(R.drawable.chat, "CONNECT AND INTERACT", "Communicate freely with each other and plan for you next Wish like never before!"));
        models.add(new Model(R.drawable.connect, "PERSONALISED FEED", "Get feeds tailored for your inner most desires!! Target and find your desired items"));
        models.add(new Model(R.drawable.trading, "GET REWARDED", "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."));
        models.add(new Model(R.drawable.connect, "WELCOME", "Welcome to WishHub"));

        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(70, 0, 70, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        enterLoginPage = findViewById(R.id.btnLoginSplash);
        enterRegisterPage = findViewById(R.id.btnRegister);

        enterLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreen.this, Login.class));
            }
        });

        enterRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}