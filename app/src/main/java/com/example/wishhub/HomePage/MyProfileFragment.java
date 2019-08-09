package com.example.wishhub.HomePage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wishhub.Authentication.EditProfile;
import com.example.wishhub.Authentication.User;
import com.example.wishhub.ChatSystem.Chat;
import com.example.wishhub.R;
import com.example.wishhub.SplashScreen.SplashScreenActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class MyProfileFragment extends Fragment {

    private Button logout;
    private FloatingActionButton editprofile ;
    private CircleImageView your_pic;
    private TextView accountname, joindate, bio, numOfPosts;
    private FirebaseUser firebaseUser;
    private ImageButton chatButton;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private List<Post> postList;
    private PostAdpapter postAdapter;
    private ProgressBar progress_circular;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_profile_page);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        // mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);

        postList = new ArrayList<>();
        postAdapter = new PostAdpapter(getContext(), postList, 1);
        recyclerView.setAdapter(postAdapter);
        progress_circular = view.findViewById(R.id.progress_circular);
        bio = view.findViewById(R.id.profilebio);
        numOfPosts = view.findViewById(R.id.t5);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("users_photos");
        reference = FirebaseDatabase.getInstance().getReference("users_names").child(firebaseUser.getUid());

        your_pic = view.findViewById(R.id.your_pic);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("users_names").child(firebaseUser.getUid());

        accountname = view.findViewById(R.id.account_name);
        joindate = view.findViewById(R.id.joindate);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (isAdded()){
                    //Load image if the fragment is currently added to its activity.
                    User user = dataSnapshot.getValue(User.class);
                    accountname.setText(user.getName());
                    joindate.setText(user.getJoindate());
                    bio.setText(user.getBio());
                    if (user.getImageURL().equals("default")){
                        your_pic.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(getContext()).load(user.getImageURL()).into(your_pic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editprofile = view.findViewById(R.id.editPofile);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfile.class));
            }
        });

        chatButton = view.findViewById(R.id.chatButton);
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
                        startActivity(new Intent(getContext(), Chat.class));
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
        return view;
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
                    if (post.getPublisher().equals(firebaseUser.getUid())){
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
                    if (post.getPublisher().equals(firebaseUser.getUid())){
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
