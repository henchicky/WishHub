package com.example.wishhub.ChatSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishhub.Authentication.User;
import com.example.wishhub.Notification.APIService;
import com.example.wishhub.Notification.Client;
import com.example.wishhub.Notification.Data;
import com.example.wishhub.Notification.MyResponse;
import com.example.wishhub.Notification.Sender;
import com.example.wishhub.Notification.Token;
import com.example.wishhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoom extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private String name, temp_key;
    private DatabaseReference root;
    private MessageAdapter messageAdapter;
    private List<Message> mChat;
    private ImageButton btn_send;
    private DatabaseReference ref;
    private FirebaseUser fuser;
    private TextView userChat, statusReport;
    private RecyclerView recyclerView;
    private APIService apiService;
    private DatabaseReference reference;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_send = findViewById(R.id.sendButton);
        final EditText text_send = findViewById(R.id.messageArea);

        userChat = findViewById(R.id.userChat);
        statusReport = findViewById(R.id.statusReport);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        name = getIntent().getExtras().getString("user_name");

        apiService = Client.getClient("https://fcm.google.apis.com/").create(APIService.class);

        root = FirebaseDatabase.getInstance().getReference();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                if (!text_send.getText().toString().equals("")) {
                    Map<String, Object> map = new HashMap<>();

                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    map2.put("receiver", name);
                    map2.put("message", text_send.getText().toString());

                    root.child("Chats").push().setValue(map2);
                    text_send.getText().clear();
                }
            }
        });

        final String msg = text_send.getText().toString();

        reference = FirebaseDatabase.getInstance().getReference("user_names").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotification(name, user.getName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users_names").child(name);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User value = dataSnapshot.getValue(User.class);
                userChat.setText("Chat with " + value.getName());
                if (value.getStatus().equals("online")) {
                    statusReport.setVisibility(View.VISIBLE);
                } else {
                    statusReport.setVisibility(View.GONE);
                }
                readMessage(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void readMessage(final User currentUser) {
        mChat = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            && message.getSender().equals(name) || message.getReceiver().equals(name)
                            && message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        mChat.add(message);
                    }
                    messageAdapter = new MessageAdapter(ChatRoom.this, mChat, currentUser.getImageURL());
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void status(String status) {
        ref = FirebaseDatabase.getInstance().getReference("users_names").child(fuser.getUid());

        String date, time;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currDate = new SimpleDateFormat("MMM dd, yyyy");
        date = currDate.format(calendar.getTime());

        SimpleDateFormat currTime = new SimpleDateFormat("hh:mm a");
        time = currTime.format(calendar.getTime());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        hashMap.put("date", date);
        hashMap.put("time", time);

        ref.updateChildren(hashMap);
    }

    protected void onResume() {
        super.onResume();
        status("online");
    }

    protected void onPause() {
        super.onPause();
        status("offline");
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    private void sendNotification(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username + ": " + message, "New Message", name);

                    Sender sender = new Sender (data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(ChatRoom.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
