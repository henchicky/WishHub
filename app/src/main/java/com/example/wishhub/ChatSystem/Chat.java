package com.example.wishhub.ChatSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wishhub.Authentication.User;
import com.example.wishhub.Notification.Token;
import com.example.wishhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity implements UserAdapter.onNoteListener {

    private static final String TAG = "Chat";
    private DatabaseReference root, reference;
    private UserAdapter userAdapter;
    private List<User> list_of_users = new ArrayList<>();
    private CircleImageView profileImage;
    private TextView profileName;
    private FirebaseUser firebaseUser;
    private Context context;
    Toolbar toolbar;
    private ImageView backhome;
    private List<Chatlist> usersList;
    RecyclerView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        usersList = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Inbox");
        setSupportActionBar(toolbar);
        backhome = findViewById(R.id.backhome);

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.username);

        messageList = findViewById(R.id.messageList);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //messageList.setAdapter(new UserAdapter(Chat.this, list_of_users, (UserAdapter.onNoteListener) context, true));
        context = this;

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void onNoteClick(int position) {
        User chosenUser = list_of_users.get(position);
        Intent intent = new Intent(this, ChatRoom.class);
        intent.putExtra("user_name", chosenUser.getId());
        startActivity(intent);
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    private void chatList() {
        list_of_users = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("users_names");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_of_users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList) {
                        if (user.getId().equals(chatlist.getId())) {
                            list_of_users.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(Chat.this, list_of_users, (UserAdapter.onNoteListener) context, true);
                messageList.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
