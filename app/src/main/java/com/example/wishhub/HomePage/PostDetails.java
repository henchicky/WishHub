package com.example.wishhub.HomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wishhub.ChatSystem.Chat;
import com.example.wishhub.ChatSystem.ChatRoom;
import com.example.wishhub.ProfileOthers;
import com.example.wishhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostDetails extends AppCompatActivity {

    private Button editBtn, chatBtn;
    private TextView description, date, likes, location, title, condition, price, publisher, numoflikes;
    private Toolbar toolbar;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Post post;
    private ImageView image, heartRed, heartWhite, backtohome;
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        post = (Post) intent.getExtras().getSerializable("post_details");

        heartWhite = findViewById(R.id.likebtn);
        heartRed = findViewById(R.id.likebtnred);
        editBtn = findViewById(R.id.edit_btn);
        chatBtn = findViewById(R.id.chatBtn1);
        description = findViewById(R.id.descriptionprod);
        date = findViewById(R.id.dateprod);
        likes = findViewById(R.id.likesprod);
        location = findViewById(R.id.locationprod);
        title = findViewById(R.id.titleprod);
        image = findViewById(R.id.imageprod);
        condition = findViewById(R.id.conditionprod);
        price = findViewById(R.id.priceprod);
        backtohome = findViewById(R.id.backtohome);
        publisher = findViewById(R.id.publisherprod);
        location = findViewById(R.id.locationprod);
        numoflikes = findViewById(R.id.numoflikes);

        publisher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileOthers.class);
                intent.putExtra("userid", post.getPublisher());
                startActivity(intent);
            }
        });

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title.setText(post.getTitle());
        description.setText(post.getDescription());
        date.setText(post.getUploaddate() + " by ");
        if (post.getItemcondition().equals("true")) {
            condition.setText("New");
        } else {
            condition.setText("Used");
        }
        price.setText("S$" + post.getPrice());
        publisher.setText(post.getName());
        if (post.getMeetup().equals("true")) {
            if (post.getDelivery().equals("true")) {
                location.setText("Meet-up@ \n" + post.getLocation() + "\n \nDelivery & Mailing available");
            } else {
                location.setText("Meet-up@ \n" + post.getLocation() + "\n \nDelivery & Mailing unavailable");
            }
        } else {
            location.setText("Only Delivery and Mailing");
        }

        Glide.with(getApplicationContext()).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(image);

        Zoomy.Builder builder = new Zoomy.Builder(this).target(image);
        builder.register();

        //check if post is liked or not
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid());
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    heartRed.setVisibility(View.VISIBLE);
                    heartWhite.setVisibility(View.GONE);
                    heartRed.setTag("liked");
                } else{
                    heartRed.setVisibility(View.GONE);
                    heartWhite.setVisibility(View.VISIBLE);
                    heartRed.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //set the number of likes
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount() + " likes");
                numoflikes.setText(""+dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        heartWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                        .child(firebaseUser.getUid()).setValue(true);

                AnimatorSet animationSet = new AnimatorSet();

                if (heartRed.getVisibility() == View.GONE) {
                    heartRed.setScaleX(0.1f);
                    heartRed.setScaleY(0.1f);

                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(heartRed, "scaleY", 0.1f, 1f);
                    scaleDownY.setDuration(300);
                    scaleDownY.setInterpolator(DECCELERATE_INTERPOLATOR);

                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(heartRed, "scaleX", 0.1f, 1f);
                    scaleDownX.setDuration(300);
                    scaleDownX.setInterpolator(DECCELERATE_INTERPOLATOR);

                    heartRed.setVisibility(View.VISIBLE);
                    heartWhite.setVisibility(View.GONE);

                    animationSet.playTogether(scaleDownY, scaleDownX);
                }
                animationSet.start();
            }
        });

        heartRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                        .child(firebaseUser.getUid()).removeValue();
                AnimatorSet animationSet = new AnimatorSet();

                if (heartRed.getVisibility() == View.VISIBLE) {
                    heartRed.setScaleX(0.1f);
                    heartRed.setScaleY(0.1f);

                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(heartRed, "scaleY", 1f, 0f);
                    scaleDownY.setDuration(300);
                    scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(heartRed, "scaleX", 1f, 0f);
                    scaleDownX.setDuration(300);
                    scaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);

                    heartRed.setVisibility(View.GONE);
                    heartWhite.setVisibility(View.VISIBLE);

                    animationSet.playTogether(scaleDownY, scaleDownX);
                }
                animationSet.start();
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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid().equals(post.getPublisher())) {
            chatBtn.setVisibility(View.GONE);
        }
    }
}
