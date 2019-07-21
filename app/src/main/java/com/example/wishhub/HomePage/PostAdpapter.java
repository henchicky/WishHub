package com.example.wishhub.HomePage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wishhub.Authentication.User;
import com.example.wishhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdpapter extends RecyclerView.Adapter<PostAdpapter.ImageViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    private FirebaseUser firebaseUser;

    public PostAdpapter (Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdpapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPosts.get(position);

        Glide.with(mContext).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(holder.post_image);

        publisherInfo(holder.image_profile, holder.username, holder.publisher, post.getPublisher());
        loadInfo(post, holder.title, holder.price, holder.condition, holder.date);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Liked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostDetails.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image, like;
        public TextView username, publisher, title, price, condition, date;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            post_image = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            publisher = itemView.findViewById(R.id.publisher);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            condition = itemView.findViewById(R.id.condition);
            date = itemView.findViewById(R.id.uploadeddate);
            imageView = itemView.findViewById(R.id.post_image);
        }
    }

    private void loadInfo(final Post post, final TextView title, final TextView price, final TextView condition, final TextView date) {
        title.setText(post.getTitle());
        price.setText(post.getPrice());
        if (post.getItemcondition().equals("true")) {
            condition.setText(" ~ New");
        } else {
            condition.setText(" ~ Used");
        }
        date.setText(post.getUploaddate());
    }

    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users_names").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageURL()).into(image_profile);
                username.setText(user.getName());
                publisher.setText(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
