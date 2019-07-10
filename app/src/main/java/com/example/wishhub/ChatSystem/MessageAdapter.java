package com.example.wishhub.ChatSystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private List<Message> mChat;
    private Context mContext;
    private String imageurl;
    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Message> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public CircleImageView profile_pic;

        public ViewHolder(View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_pic = itemView.findViewById(R.id.profile_pic);
        }
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {
        final Message message = mChat.get(i);
        viewHolder.show_message.setText(message.getMessage());

        if (imageurl.equals(null)) {
            Glide.with(mContext).load("https://firebasestorage.googleapis.com/v0/b/blogapp-72a37.appspot.com/o/users_photos%2F1562210317021.png?alt=media&token=25a21746-3194-43ab-97a4-102003162a6a").into(viewHolder.profile_pic);
        } else {
            //Glide.with(mContext).load("https://firebasestorage.googleapis.com/v0/b/blogapp-72a37.appspot.com/o/users_photos%2F1562210317021.png?alt=media&token=25a21746-3194-43ab-97a4-102003162a6a").into(viewHolder.profile_pic);
            Glide.with(mContext).load(imageurl).into(viewHolder.profile_pic);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}
