package com.example.wishhub.ChatSystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wishhub.Authentication.User;
import com.example.wishhub.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> mUsers;
    private onNoteListener monnoteListener;
    private Context mContext;
    private boolean ischat;


    public UserAdapter(Context mContext, List<User> mUsers, onNoteListener onnoteListener, boolean ischat) {
        this.mUsers = mUsers;
        this.monnoteListener = onnoteListener;
        this.mContext = mContext;
        this.ischat = ischat;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView username;
        public ImageView profile_image;
        public RelativeLayout parentLayout;
        onNoteListener onnoteListener;
        private ImageView img_off;
        private ImageView img_on;

        public ViewHolder(View itemView, onNoteListener onnoteListener) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onnoteListener = onnoteListener;
            itemView.setOnClickListener(this);
            img_off = itemView.findViewById(R.id.img_off);
            img_on = itemView.findViewById(R.id.img_on);
        }

        @Override
        public void onClick(View v) {
            onnoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_layout, viewGroup, false);
        return new ViewHolder(view, monnoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = mUsers.get(i);
        viewHolder.username.setText(user.getName());
        Glide.with(mContext).load(user.getImageURL()).into(viewHolder.profile_image);

        if (ischat) {
            if (user.getStatus().equals("online")) {
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            } else {
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.img_off.setVisibility(View.GONE);
            viewHolder.img_on.setVisibility(View.GONE);
        }
    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}

