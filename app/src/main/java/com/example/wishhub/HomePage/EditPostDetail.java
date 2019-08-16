package com.example.wishhub.HomePage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wishhub.Miscellaneous.CurrencyEditText;
import com.example.wishhub.ProfileOthers;
import com.example.wishhub.R;
import com.google.android.material.textfield.TextInputEditText;

public class EditPostDetail extends AppCompatActivity {

    private static final String TAG = "EditPostDetails";
    private Post post;
    private ImageView back, save;
    private Switch delivery, meetup, itemcondition;
    private TextView condition;
    private TextInputEditText title, description;
    private CurrencyEditText price;
    private ImageView image;
    private Button saveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_detail);

        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("edit_post_details");
        //Toast.makeText(this, "post is " + post.getTitle(), Toast.LENGTH_SHORT).show();

        back = findViewById(R.id.backtoprofile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*save = findViewById(R.id.savesucc);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditPostDetail.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });*/

        delivery = findViewById(R.id.switch3);
        meetup = findViewById(R.id.switch2);
        itemcondition = findViewById(R.id.switch1);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image_hide);
        saveChanges = findViewById(R.id.save_changes);

        Log.d(TAG, "onCreate: " + post.getTitle());

        /*
        if (post.getDelivery().equals("true")) {
            delivery.setChecked(true);
        } else {
            delivery.setChecked(false);
        }*/
        if (post.getMeetup().equals("true")) {
            meetup.setChecked(true);
        } else {
            meetup.setChecked(false);
        }
        if (post.getItemcondition().equals("true")) {
            itemcondition.setChecked(true);
            itemcondition.setText("Listing Condition: Item is new!");
        } else {
            itemcondition.setChecked(false);
            itemcondition.setText("Listing Condition: Item is used!");
        }
        title.setText(post.getTitle());
        description.setText(post.getDescription());
        price.setText("S$ " + post.getPrice());
        Glide.with(getApplicationContext()).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(image);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditPostDetail.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
