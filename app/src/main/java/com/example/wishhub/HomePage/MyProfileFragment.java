package com.example.wishhub.HomePage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.wishhub.R;
import com.example.wishhub.SplashScreen.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileFragment extends Fragment {

    private Button logout;
    private CircleImageView your_pic;


    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myprofile, container, false);
        logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity().getApplicationContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity().getApplicationContext(), SplashScreen.class));
            }
        });

        your_pic = view.findViewById(R.id.your_pic);
        your_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Change profile pic", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
