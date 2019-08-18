package com.example.wishhub.HomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wishhub.Authentication.EditProfile;
import com.example.wishhub.Miscellaneous.CurrencyEditText;
import com.example.wishhub.ProfileOthers;
import com.example.wishhub.R;
import com.example.wishhub.SplashScreen.SplashScreenActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

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
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    private StorageReference storageRef;
    boolean item_condition = true;
    boolean item_meetup = true;
    boolean item_delivery = true;
    boolean image_untouched = true;

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


        if (post.getDelivery().equals("true")) {
            delivery.setChecked(true);
            item_delivery = true;
        } else {
            delivery.setChecked(false);
            item_delivery = false;
        }
        delivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        if (post.getItemcondition().equals("true")) {
            itemcondition.setChecked(true);
            itemcondition.setText("Listing Condition: Item is new!");
            item_condition = true;
        } else {
            itemcondition.setChecked(false);
            itemcondition.setText("Listing Condition: Item is used!");
            item_condition = false;
        }
        itemcondition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    itemcondition.setText("Listing Condition: Item is new!");
                    item_condition = true;
                } else {
                    itemcondition.setText("Listing Condition: Item is used!");
                    item_condition = false;
                }
            }
        });

        if (post.getMeetup().equals("true")) {
            meetup.setChecked(true);
            meetup.setText("Meet-up: Available for meet-up");
            locationlayout.setVisibility(View.VISIBLE);
            location.setText(post.getLocation());
            item_meetup = true;
        } else {
            meetup.setChecked(false);
            meetup.setText("Meet-up: Unavailable");
            locationlayout.setVisibility(View.GONE);
            item_meetup = false;
        }
        meetup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    meetup.setText("Meet-up: Available for meet-up");
                    locationlayout.setVisibility(View.VISIBLE);
                    item_condition = false;
                } else {
                    meetup.setText("Meet-up: Unavailable");
                    locationlayout.setVisibility(View.GONE);
                    item_condition = false;
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
                image_untouched = false;
            }
        });

        storageRef = FirebaseStorage.getInstance().getReference("posts");


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (image_untouched) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                    String postid = post.getPostid();

                    final HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("postid", postid);

                    //hashMap.put("postimage", photoStringLink);
                    hashMap.put("title", title.getText().toString());
                    hashMap.put("description", description.getText().toString());
                    hashMap.put("price", price.getText().toString().substring(3));
                    hashMap.put("itemcondition", "" + item_condition);
                    hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //hashMap.put("uploaddate", date);
                    hashMap.put("mailing", "" + item_delivery);
                    hashMap.put("location", location.getText().toString());
                    hashMap.put("meetup", "" + item_meetup);
                    //hashMap.put("name", GetName.namevar);
                    hashMap.put("delivery", "" + item_delivery);

                    reference.child(post.getPostid()).updateChildren(hashMap);
                    //pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    ImageUri imageUriupload = new ImageUri(mImageUri);
                    final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(imageUriupload.getImageUri()));
                    uploadTask = fileReference.putFile(imageUriupload.getImageUri());

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return fileReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                //System.out.println("Upload " + downloadUri);
                                //Toast.makeText(getActivity(), "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                if (downloadUri != null) {
                                    String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                                    String postid = post.getPostid();

                                    final HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("postid", postid);

                                    hashMap.put("postimage", photoStringLink);
                                    hashMap.put("title", title.getText().toString());
                                    hashMap.put("description", description.getText().toString());
                                    hashMap.put("price", price.getText().toString().substring(3));
                                    hashMap.put("itemcondition", "" + item_condition);
                                    hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    //hashMap.put("uploaddate", date);
                                    hashMap.put("mailing", "" + item_delivery);
                                    hashMap.put("location", location.getText().toString());
                                    hashMap.put("meetup", "" + item_meetup);
                                    //hashMap.put("name", GetName.namevar);
                                    hashMap.put("delivery", "" + item_delivery);

                                    reference.child(post.getPostid()).updateChildren(hashMap);
                                    //pd.dismiss();
                                    Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // Handle failures
                            }
                            finish();
                        }
                    });
                }
            }
        });

        deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditPostDetail.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Sold listings appear on your profile to give other members a sense of your style and successful track record. " +
                        "Deleting cannot be undone.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //progressBar.setVisibility(View.VISIBLE);
                        FirebaseDatabase.getInstance().getReference("Posts").child(post.getPostid()).removeValue();
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        Toast.makeText(EditPostDetail.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(i);
                    }
                });

                dialog.setNegativeButton("Don't Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
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

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
