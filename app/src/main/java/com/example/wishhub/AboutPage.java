package com.example.wishhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutPage extends AppCompatActivity {

    private ImageView back;
    private TextView telegramlink, githublink, emaillink, emaillink2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        back = findViewById(R.id.backtoprofile);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        telegramlink = findViewById(R.id.telegramlink);
        githublink = findViewById(R.id.githublink);
        emaillink = findViewById(R.id.henemail);
        emaillink2 = findViewById(R.id.changemail);

        telegramlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://t.me/hendrickling");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

    }
}
