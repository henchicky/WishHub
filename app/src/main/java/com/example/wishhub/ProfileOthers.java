package com.example.wishhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wishhub.Authentication.User;
import com.example.wishhub.ChatSystem.Chat;
import com.example.wishhub.HomePage.GetName;
import com.example.wishhub.HomePage.Post;
import com.example.wishhub.HomePage.PostAdpapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileOthers extends AppCompatActivity {

    private static final String TAG = "Profile Others";
    private String userid;
    private DatabaseReference reference;
    private CircleImageView your_pic;
    private TextView accountname, joindate, bio, mylistingtext, numOfPosts;
    private FirebaseUser firebaseUser;
    private ImageButton chatButton;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private RecyclerView recyclerView;
    private List<Post> postList;
    private PostAdpapter postAdapter;
    private ProgressBar progress_circular;
    private ImageView backlogo;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_others);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        Log.d(TAG, "userid: " + userid);

        recyclerView = findViewById(R.id.recycler_view_profile_page);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdpapter(getApplicationContext(), postList, 1);
        recyclerView.setAdapter(postAdapter);
        progress_circular = findViewById(R.id.progress_circular);
        bio = findViewById(R.id.profilebio);
        backlogo = findViewById(R.id.logo);
        backlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference("users_photos");

        your_pic = findViewById(R.id.your_pic);
        accountname = findViewById(R.id.account_name);
        joindate = findViewById(R.id.joindate);
        mylistingtext = findViewById(R.id.mylistingtext);
        numOfPosts = findViewById(R.id.tnumofPosts);

        reference = FirebaseDatabase.getInstance().getReference("users_names").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                accountname.setText(user.getName());
                mylistingtext.setText(user.getName().toUpperCase() + "'S LISTING");
                joindate.setText(user.getJoindate());
                bio.setText(user.getBio());
                if (user.getImageURL().equals("default")){
                    your_pic.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(your_pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chatButton = findViewById(R.id.chatButton);
        chatButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        startActivity(new Intent(getApplicationContext(), Chat.class));
                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        readPosts();
        numOfPosts();
    }

    private void numOfPosts() {
        GetName.number_posts = 0;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    //only see your own post in profile page
                    if (post.getPublisher().equals(userid)){
                        GetName.number_posts++;
                    }
                }
                numOfPosts.setText(GetName.number_posts + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    //only see your own post in profile page
                    if (post.getPublisher().equals(userid)){
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
                progress_circular.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
