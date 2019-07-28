package com.example.wishhub.HomePage;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
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
    private int type;

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    private FirebaseUser firebaseUser;

    public PostAdpapter (Context context, List<Post> posts, int type) {
        mContext = context;
        mPosts = posts;
        this.type = type;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_owner, parent, false);
            return new PostAdpapter.ImageViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
            return new PostAdpapter.ImageViewHolder(view);
        }
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

        final AnimatorSet animationSet =  new AnimatorSet();

        holder.heartRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.heartRed.getVisibility() == View.VISIBLE) {
                    holder.heartRed.setScaleX(0.1f);
                    holder.heartRed.setScaleY(0.1f);

                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(holder.heartRed, "scaleY", 1f, 0f);
                    scaleDownY.setDuration(300);
                    scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(holder.heartRed, "scaleX", 1f, 0f);
                    scaleDownX.setDuration(300);
                    scaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);

                    holder.heartRed.setVisibility(View.GONE);
                    holder.heartWhite.setVisibility(View.VISIBLE);

                    animationSet.playTogether(scaleDownY, scaleDownX);
                } else if (holder.heartRed.getVisibility() == View.GONE) {
                    holder.heartRed.setScaleX(0.1f);
                    holder.heartRed.setScaleY(0.1f);

                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(holder.heartRed, "scaleY", 0.1f, 1f);
                    scaleDownY.setDuration(300);
                    scaleDownY.setInterpolator(DECCELERATE_INTERPOLATOR);

                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(holder.heartRed, "scaleX", 0.1f, 1f);
                    scaleDownX.setDuration(300);
                    scaleDownX.setInterpolator(DECCELERATE_INTERPOLATOR);

                    holder.heartRed.setVisibility(View.VISIBLE);
                    holder.heartWhite.setVisibility(View.GONE);

                    animationSet.playTogether(scaleDownY, scaleDownX);
                }
                animationSet.start();
            }
        });

        holder.heartWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.heartRed.getVisibility() == View.VISIBLE) {
                    holder.heartRed.setScaleX(0.1f);
                    holder.heartRed.setScaleY(0.1f);

                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(holder.heartRed, "scaleY", 1f, 0f);
                    scaleDownY.setDuration(300);
                    scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(holder.heartRed, "scaleX", 1f, 0f);
                    scaleDownX.setDuration(300);
                    scaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);

                    holder.heartRed.setVisibility(View.GONE);
                    holder.heartWhite.setVisibility(View.VISIBLE);

                    animationSet.playTogether(scaleDownY, scaleDownX);
                } else if (holder.heartRed.getVisibility() == View.GONE) {
                    holder.heartRed.setScaleX(0.1f);
                    holder.heartRed.setScaleY(0.1f);

                    ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(holder.heartRed, "scaleY", 0.1f, 1f);
                    scaleDownY.setDuration(300);
                    scaleDownY.setInterpolator(DECCELERATE_INTERPOLATOR);

                    ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(holder.heartRed, "scaleX", 0.1f, 1f);
                    scaleDownX.setDuration(300);
                    scaleDownX.setInterpolator(DECCELERATE_INTERPOLATOR);

                    holder.heartRed.setVisibility(View.VISIBLE);
                    holder.heartWhite.setVisibility(View.GONE);

                    animationSet.playTogether(scaleDownY, scaleDownX);
                }
                animationSet.start();
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostDetails.class);
                intent.putExtra("post_details", post);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image, heartRed, heartWhite;
        public TextView username, publisher, title, price, condition, date;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            post_image = itemView.findViewById(R.id.post_image);
            heartRed = itemView.findViewById(R.id.likebtnchange);
            heartWhite = itemView.findViewById(R.id.likebtnred);
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
        price.setText("S$" + post.getPrice());
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
                Glide.with(mContext.getApplicationContext()).load(user.getImageURL()).into(image_profile);
                username.setText(user.getName());
                publisher.setText(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
