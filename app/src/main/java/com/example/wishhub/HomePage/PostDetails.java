package com.example.wishhub.HomePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wishhub.ChatSystem.Chat;
import com.example.wishhub.ChatSystem.ChatRoom;
import com.example.wishhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostDetails extends AppCompatActivity {

    private Button editBtn, chatBtn;
    private TextView description, date, likes, location, title, condition;
    private Toolbar toolbar;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Post post;
    private ImageView image, likebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        post = (Post) intent.getExtras().getSerializable("post_details");

        likebtn = findViewById(R.id.likebtn);
        editBtn = findViewById(R.id.edit_btn);
        chatBtn = findViewById(R.id.chatBtn1);
        description = findViewById(R.id.descriptionprod);
        date = findViewById(R.id.dateprod);
        likes = findViewById(R.id.likesprod);
        location = findViewById(R.id.locationprod);
        title = findViewById(R.id.titleprod);
        image = findViewById(R.id.imageprod);
        condition = findViewById(R.id.conditionprod);

        title.setText(post.getTitle());
        description.setText(post.getDescription());
        date.setText(post.getUploaddate());
        if (post.getItemcondition().equals("true")) {
            condition.setText("New");
        } else {
            condition.setText("Used");
        }

        Glide.with(getApplicationContext()).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(image);

        Zoomy.Builder builder = new Zoomy.Builder(this).target(image);
        builder.register();

        likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetails.this, "Liked", Toast.LENGTH_SHORT).show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetails.this, "Edit Post", Toast.LENGTH_SHORT).show();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatRoom.class);
                intent.putExtra("user_name", post.getPublisher());
                startActivity(intent);
            }
        });
    }
}
