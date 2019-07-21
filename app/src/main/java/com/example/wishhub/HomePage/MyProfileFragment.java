package com.example.wishhub.HomePage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wishhub.Authentication.User;
import com.example.wishhub.R;
import com.example.wishhub.SplashScreen.SplashScreenActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends Fragment {

    private Button logout;
    private CircleImageView your_pic;
    private TextView accountname, joindate;
    private FirebaseUser firebaseUser;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);
        logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity().getApplicationContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity().getApplicationContext(), SplashScreenActivity.class));
            }
        });

        your_pic = view.findViewById(R.id.your_pic);
        your_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Change profile pic", Toast.LENGTH_LONG).show();
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users_names").child(firebaseUser.getUid());

        accountname = view.findViewById(R.id.account_name);
        joindate = view.findViewById(R.id.joindate);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getImageURL()).into(your_pic);
                accountname.setText(user.getName());
                joindate.setText("Joined Date: " + user.getJoindate());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }
}
