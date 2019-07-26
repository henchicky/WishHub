package com.example.wishhub.HomePage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wishhub.ChatSystem.Chat;
import com.example.wishhub.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<ImageUri> imageList;
    private Context mContext;
    private ImageButton deleteBtn;


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.uploaded_image);
            Zoomy.Builder builder = new Zoomy.Builder((Activity) itemView.getContext()).target(mImageView);
            builder.register();
        }
    }

    public ImageAdapter(ArrayList<ImageUri> exampleList, Context mContext) {
        imageList = exampleList;
        this.mContext = mContext;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_item_layout, parent, false);
        ImageViewHolder evh = new ImageViewHolder(v);
        deleteBtn = v.findViewById(R.id.delete_image);
        return evh;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        if (position == imageList.size()) {
            deleteBtn.setVisibility(View.GONE);
        } else {
            deleteBtn.setVisibility(View.VISIBLE);
        }

        ImageUri currentItem = imageList.get(position);

        Glide.with(mContext).load(currentItem.getImageUri())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(holder.mImageView);

        deleteBtn.setOnTouchListener(new View.OnTouchListener() {
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
                        //Toast.makeText(mContext, "Delete the pic at postion "+ position, Toast.LENGTH_SHORT).show();
                        imageList.remove(position);
                        notifyItemRemoved(position);
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
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}