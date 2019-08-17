package com.example.wishhub.HomePage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wishhub.Miscellaneous.CurrencyEditText;
import com.example.wishhub.ProfileOthers;
import com.example.wishhub.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

public class EditPostDetail extends AppCompatActivity {

    private static final String TAG = "EditPostDetails";
    private Post post;
    private ImageView back, save;
    private Switch delivery, meetup, itemcondition;
    private TextView condition;
    private TextInputEditText title, description;
    private CurrencyEditText price;
    private ImageView image;
    private Button saveChanges, deletePost;
    private Uri mImageUri, tempUri;
    private TextInputLayout locationlayout;
    private TextInputEditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_detail);

        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("edit_post_details");
        //Toast.makeText(this, "post is " + post.getTitle(), Toast.LENGTH_SHORT).show();

        back = findViewById(R.id.backtoprofile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delivery = findViewById(R.id.switch3);
        meetup = findViewById(R.id.switch2);
        itemcondition = findViewById(R.id.switch1);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image_hide);
        saveChanges = findViewById(R.id.save_changes);
        deletePost = findViewById(R.id.delete_post);
        locationlayout = findViewById(R.id.tt5);
        location = findViewById(R.id.locationprod);

        Log.d(TAG, "onCreate: " + post.getTitle());

        /*
        if (post.getDelivery().equals("true")) {
            delivery.setChecked(true);
        } else {
            delivery.setChecked(false);
        }*/
        if (post.getItemcondition().equals("true")) {
            itemcondition.setChecked(true);
            itemcondition.setText("Listing Condition: Item is new!");
        } else {
            itemcondition.setChecked(false);
            itemcondition.setText("Listing Condition: Item is used!");
        }
        itemcondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    itemcondition.setText("Listing Condition: Item is new!");
                } else {
                    itemcondition.setText("Listing Condition: Item is used!");
                }
            }
        });

        if (post.getMeetup().equals("true")) {
            meetup.setChecked(true);
            meetup.setText("Meet-up: Available for meet-up");
            locationlayout.setVisibility(View.VISIBLE);
            location.setText(post.getLocation());
        } else {
            meetup.setChecked(false);
            meetup.setText("Meet-up: Unavailable");
            locationlayout.setVisibility(View.GONE);
        }
        meetup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    meetup.setText("Meet-up: Available for meet-up");
                    locationlayout.setVisibility(View.VISIBLE);
                } else {
                    meetup.setText("Meet-up: Unavailable");
                    locationlayout.setVisibility(View.GONE);
                }
            }
        });

        title.setText(post.getTitle());
        description.setText(post.getDescription());
        price.setText("S$ " + post.getPrice());
        Glide.with(getApplicationContext()).load(post.getPostimage())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity(mImageUri).setAspectRatio(1,1).getIntent(getApplicationContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditPostDetail.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditPostDetail.this, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            Glide.with(getApplicationContext()).load(mImageUri)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                    .into(image);
            //imageURLlist.add(imageURLlist.size(), new ImageUri(mImageUri));
            //mAdapter.notifyItemInserted(imageURLlist.size() - 1);
            //mAdapter.notifyDataSetChanged();
            //Toast.makeText(getContext(), "Added to arraylist" + imageURLlist.size(), Toast.LENGTH_SHORT).show();
            mImageUri = tempUri;

            //Toast.makeText(getApplicationContext(), "imageURLlist length = "+imageURLlist.size(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Something gone wrong!", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(getContext(), HomeActivity.class));
        }
    }
}
