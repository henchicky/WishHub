package com.example.wishhub.HomePage;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wishhub.Authentication.User;
import com.example.wishhub.Miscellaneous.CurrencyEditText;
import com.example.wishhub.R;
import com.github.zagum.switchicon.SwitchIconView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.polyak.iconswitch.IconSwitch;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class TradeFragment extends Fragment {

    private static final String TAG = "Trade Fragment";
    private ArrayList<ImageUri> imageURLlist;
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private TextView uploadmulitpleimages, text_description;
    private Uri mImageUri, tempUri;
    private ImageButton deletepic;
    private TextView textty, title_listing, bottom_wish;
    private TextInputEditText title_text, description_text;
    private TextInputLayout priceInputLayout, titleinputlayout, descinputlayout, locationlayout;
    ImageView hideimage;

    String miUrlOk = "";
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    StorageReference storageRef;
    Button post;
    TextInputEditText description, title, location;
    CurrencyEditText price;
    Switch switch_condition, switch_meetup, switch_delivery;
    boolean item_condition = true;
    boolean item_meetup = true;
    boolean item_delivery = true;
    boolean item_wish = true;
    TextInputLayout priceinput;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private static List<String> listOfUrl;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference, secondref;
    private SwitchIconView icon_condition, icon_meetup, icon_delivery;
    private TextView text_delivery, text_meetup, text_condition;
    private IconSwitch iconSwitch;
    TabLayout toolbar;
    private FrameLayout frameLayout;
    private BottomSheetBehavior sheetBehavior;
    private View view_block;
    private ImageView left_arrow, right_arrow;
    private RelativeLayout relativeLayout;

    public TradeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade, container, false);

        createList();
        Window window = getActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));

        textty = view.findViewById(R.id.text_upload);
        deletepic = view.findViewById(R.id.delete_image);
        mRecyclerView = view.findViewById(R.id.recyclerView_hor);
        mRecyclerView.setHasFixedSize(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        secondref = FirebaseDatabase.getInstance().getReference("users_names").child(firebaseUser.getUid());

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

        hideimage = view.findViewById(R.id.image_hide);
        post = view.findViewById(R.id.post);
        description = view.findViewById(R.id.description);
        title = view.findViewById(R.id.title);
        price = view.findViewById(R.id.price);
        priceInputLayout = view.findViewById(R.id.priceinput);
        titleinputlayout = view.findViewById(R.id.tt1);
        descinputlayout = view.findViewById(R.id.tt2);
        locationlayout = view.findViewById(R.id.tt5);
        location = view.findViewById(R.id.locationprod);

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

        listOfUrl = new ArrayList<>();

        icon_condition = view.findViewById(R.id.switch_icon_condition);
        text_condition = view.findViewById(R.id.text_condition);
        icon_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon_condition.switchState();
                if (icon_condition.isIconEnabled()) {
                    text_condition.setText("New");
                    item_condition = true;
                } else {
                    text_condition.setText("Used");
                    item_condition = false;
                }
            }
        });

        icon_meetup = view.findViewById(R.id.switch_icon_meetup);
        text_meetup = view.findViewById(R.id.text_meetup);
        icon_meetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon_meetup.switchState();
                if (icon_meetup.isIconEnabled()) {
                    text_meetup.setText("Meetup");
                    item_meetup = true;
                    locationlayout.setVisibility(View.VISIBLE);
                } else {
                    text_meetup.setText("N.A.");
                    item_meetup = false;
                    locationlayout.setVisibility(View.GONE);
                }
            }
        });

        icon_delivery = view.findViewById(R.id.switch_icon_delivery);
        text_delivery = view.findViewById(R.id.text_delivery);
        icon_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                icon_delivery.switchState();
                if (icon_delivery.isIconEnabled()) {
                    text_delivery.setText("Delivery");
                    item_delivery = true;
                } else {
                    text_delivery.setText("N.A.");
                    item_delivery = false;
                }
            }
        });

        bottom_wish = view.findViewById(R.id.bottom_wish);
        iconSwitch = view.findViewById(R.id.icon_switch);
        title_listing = view.findViewById(R.id.title_listing);
        bottom_wish.setText("List your items!");
        title.setHint("Listing Title");
        description.setHint("Listing Description");
        view_block = view.findViewById(R.id.view_block);

        frameLayout = view.findViewById(R.id.bottom_sheet_register);
        sheetBehavior = BottomSheetBehavior.from(frameLayout);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        relativeLayout = view.findViewById(R.id.relat_layout);
        text_description = view.findViewById(R.id.text_description);

        iconSwitch.setCheckedChangeListener(new IconSwitch.CheckedChangeListener() {
            @Override
            public void onCheckChanged(IconSwitch.Checked current) {
                if (current.equals(IconSwitch.Checked.LEFT)) {
                    //Toast.makeText(getContext(), "Left toggle", Toast.LENGTH_SHORT).show();
                    title_listing.setText("Describe Your Wish");
                    title.setHint("Wish Title");
                    description.setHint("Wish Description");
                    bottom_wish.setText("Make a Wish!");
                    text_description.setText("Can't find your desired item anywhere? \nRequest for your personalised items! \nGet quotes and compare the best prices before making your decision.");
                    item_wish = true;
                    relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.pink_background));
                    //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //view_block.setVisibility(View.GONE);
                    post.setText("Make a Wish");
                } else {
                    //Toast.makeText(getContext(), "Right toggle", Toast.LENGTH_SHORT).show();
                    title_listing.setText("Describe Your Listing");
                    description.setHint("Listing Description");
                    title.setHint("Listing Title");
                    bottom_wish.setText("Sell your items!");
                    relativeLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_blue));
                    text_description.setText("Clear your clutter! Sell your beloved items.\nEarn some cash along the way! Others will benefit from your items.\nYour clutter is another's treasure!");
                    item_wish = false;
                    post.setText("Post Listing");

                    //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //view_block.setVisibility(View.GONE);
                }
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED) {
                    view_block.setVisibility(View.VISIBLE);
                } else {
                    view_block.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (isAdded()) {
                    //transitionBottomSheetBackgroundColor(v);
                    animateBottomSheetArrows(v);
                }

            }
        });

        view_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_block.setVisibility(View.GONE);
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        left_arrow = view.findViewById(R.id.image_switch_left);
        right_arrow = view.findViewById(R.id.image_switch_right);

        // Animate counter-clockwise
        left_arrow.setRotation(1 * -180);
        // Animate clockwise
        right_arrow.setRotation(1 * 180);
        return view;
    }

    private void transitionBottomSheetBackgroundColor(float slideOffset) {
        int colorFrom = ContextCompat.getColor(getContext(), R.color.light_blue);
        int colorTo = ContextCompat.getColor(getContext(), R.color.background_blue);
        relativeLayout.setBackgroundColor(interpolateColor(slideOffset,
                colorFrom, colorTo));
    }

    private int interpolateColor(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;
        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;
        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    private void animateBottomSheetArrows(float slideOffset) {
        // Animate counter-clockwise
        left_arrow.setRotation(slideOffset * -180);
        // Animate clockwise
        right_arrow.setRotation(slideOffset * 180);
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
            imageURLlist.add(imageURLlist.size(), new ImageUri(mImageUri));
            //mAdapter.notifyItemInserted(imageURLlist.size() - 1);
            mAdapter.notifyDataSetChanged();
            //Toast.makeText(getContext(), "Added to arraylist" + imageURLlist.size(), Toast.LENGTH_SHORT).show();
            mImageUri = tempUri;
            if (imageURLlist.size() > 0) {
                textty.setVisibility(View.GONE);
                hideimage.setVisibility(View.GONE);
            }
            Toast.makeText(getContext(), "imageURLlist length = "+imageURLlist.size(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Something gone wrong!", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(getContext(), HomeActivity.class));
        }
    }

    private void uploadImage_10(){
        final String pricing = price.getText().toString().trim();
        if (pricing.isEmpty() || title.getText().toString().isEmpty() || description.getText().toString().isEmpty() || item_meetup == true && location.getText().toString().isEmpty() ) {
            if (pricing.isEmpty()) {
                priceInputLayout.setError("Please enter a price.");
            }
            if (title.getText().toString().isEmpty()) {
                titleinputlayout.setError("Please enter a title");
            }
            if (description.getText().toString().isEmpty()) {
                descinputlayout.setError("Please enter a description");
            }
            if (location.getText().toString().isEmpty()) {
                locationlayout.setError("Please enter a preferred location");
            }
        } else {
            //Toast.makeText(getContext(), "ArrayList = " + imageURLlist.size(), Toast.LENGTH_SHORT).show();
            if (imageURLlist.size() > 0) {
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Posting");
                pd.show();

                if (imageURLlist.size() > 0) {
                    //1st image
                    final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(imageURLlist.get(0).getImageUri()));

                    uploadTask = fileReference.putFile(imageURLlist.get(0).getImageUri());

                    secondref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            GetName.namevar = user.getName();
                            Log.d(TAG, "onDataChange: TradeFragment: " + GetName.namevar);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

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
                                    String postid = reference.push().getKey();

                                    final HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("postid", postid);

                                    //put in a list of urls
                                    String list = "";
                                    for (int i = 0; i < listOfUrl.size(); i++) {
                                        list += listOfUrl.get(i) + "|||||";
                                    }
                                    hashMap.put("postimage", photoStringLink);
                                    hashMap.put("title", title.getText().toString());
                                    hashMap.put("description", description.getText().toString());
                                    hashMap.put("price", pricing.substring(3));
                                    hashMap.put("itemcondition", "" + item_condition);
                                    hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    hashMap.put("uploaddate", date);
                                    hashMap.put("mailing", "" + item_delivery);
                                    hashMap.put("location", location.getText().toString());
                                    hashMap.put("meetup", "" + item_meetup);
                                    hashMap.put("name", GetName.namevar);
                                    hashMap.put("delivery", "" + item_delivery);
                                    hashMap.put("wish", item_wish);
                                    hashMap.put("sold", true);

                                    reference.child(postid).setValue(hashMap);
                                    pd.dismiss();
                                    Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // Handle failures
                            }
                            startActivity(new Intent(getContext(), HomeActivity.class));
                        }
                    });
                } else if (imageURLlist.size() == 2) {
                    //1st image
                    final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(imageURLlist.get(0).getImageUri()));

                    uploadTask = fileReference.putFile(imageURLlist.get(0).getImageUri());

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
                                    listOfUrl.add(photoStringLink);
                                }

                            } else {
                                // Handle failures
                            }
                        }
                    });

                    //2nd image
                    final StorageReference fileReference2 = storageRef.child(System.currentTimeMillis()
                            + "." + getFileExtension(imageURLlist.get(1).getImageUri()));

                    uploadTask = fileReference2.putFile(imageURLlist.get(1).getImageUri());

                    Task<Uri> urlTask2 = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return fileReference2.getDownloadUrl();
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
                                    listOfUrl.add(photoStringLink);
                                }

                            } else {
                                // Handle failures
                            }
                        }
                    });
                } else {}

                /*
                Toast.makeText(getContext(), "listOfUrl size = " + listOfUrl.size(), Toast.LENGTH_SHORT).show();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                String postid = reference.push().getKey();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("postid", postid);

                //put in a list of urls
                String list = "";
                for (int i = 0; i < listOfUrl.size(); i++) {
                    list += listOfUrl.get(i) + "|||||";
                }
                hashMap.put("postimage", list);
                hashMap.put("title", title.getText().toString());
                hashMap.put("description", description.getText().toString());
                hashMap.put("price", pricing.substring(3));
                hashMap.put("itemcondition", "" + item_condition);
                hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("uploaddate", date);
                reference.child(postid).setValue(hashMap);

                pd.dismiss();*/

                //startActivity(new Intent(getContext(), HomeActivity.class));
                //getActivity().finish();
            } else {
                Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
            }



        /*
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
        }*/
        }
    }
}
