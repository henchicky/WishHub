package com.example.wishhub.HomePage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishhub.Miscellaneous.CurrencyEditText;
import com.example.wishhub.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class TradeFragment extends Fragment {

    private ArrayList<ImageUri> imageURLlist;
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private Button uploadmulitpleimages;
    private Uri mImageUri, tempUri;
    private ImageButton deletepic;
    boolean startscreen = true;
    private TextView text;

    String miUrlOk = "";
    private StorageTask uploadTask;
    StorageReference storageRef;
    TextView post;
    EditText description, title;
    CurrencyEditText price;
    Switch switch_condition;
    boolean item_condition = true;
    TextInputLayout priceinput;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    public TradeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade, container, false);

        createList();
        text = view.findViewById(R.id.text_upload);
        deletepic = view.findViewById(R.id.delete_image);
        mRecyclerView = view.findViewById(R.id.recyclerView_hor);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new ImageAdapter(imageURLlist, getContext());
        mRecyclerView.setAdapter(mAdapter);

        uploadmulitpleimages = view.findViewById(R.id.upload_multiple_images);
        uploadmulitpleimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity(mImageUri).setAspectRatio(1,1).getIntent(getContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        post = view.findViewById(R.id.post);
        description = view.findViewById(R.id.description);
        title = view.findViewById(R.id.title);
        price = view.findViewById(R.id.price);
        switch_condition = view.findViewById(R.id.switch1);
        switch_condition.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    switch_condition.setText("Listing Condition: Item is new!");
                    item_condition = true;
                } else {
                    switch_condition.setText("Listing Condition: Item is used!");
                    item_condition = false;
                }
            }
        });

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());

        priceinput = view.findViewById(R.id.priceinput);
        storageRef = FirebaseStorage.getInstance().getReference("posts");

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage_10();
            }
        });

        return view;
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void createList() {
        imageURLlist = new ArrayList<>();
        Uri uri = Uri.parse("android.resource://com.example.wishhub/drawable/ic_add");
        //imageURLlist.add(new ImageUri(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            Toast.makeText(getContext(), "array length "+imageURLlist.size(), Toast.LENGTH_SHORT).show();
            imageURLlist.add(imageURLlist.size(), new ImageUri(mImageUri));
            //mAdapter.notifyItemInserted(imageURLlist.size() - 1);
            mAdapter.notifyDataSetChanged();
            //Toast.makeText(getContext(), "Added to arraylist" + imageURLlist.size(), Toast.LENGTH_SHORT).show();
            //mImageUri = tempUri;
            if (imageURLlist.size() > 0) {
                text.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(getContext(), "Something gone wrong!", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(getContext(), HomeActivity.class));
        }
    }

    private void uploadImage_10(){

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Posting");
        pd.show();
        if (mImageUri != null){
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        miUrlOk = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postid = reference.push().getKey();

                        String pricing = price.getText().toString();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", miUrlOk);
                        hashMap.put("title", title.getText().toString());
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("price", pricing.substring(3));
                        hashMap.put("itemcondition", "" + item_condition);
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("uploaddate", date);
                        reference.child(postid).setValue(hashMap);

                        pd.dismiss();

                        startActivity(new Intent(getContext(), HomeActivity.class));
                        getActivity().finish();

                    } else {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}
