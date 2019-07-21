package com.example.wishhub.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wishhub.HomePage.HomeActivity;
import com.example.wishhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private static final int PReqcode = 1;
    static int REQUESTCODE = 1;
    //Uri pickedImgUri = Uri.parse("android.resource://com.example.wishhub/" + R.drawable.userphoto);
    private EditText etName, etEmail, etPassword, etPassword2;
    private TextInputLayout editTextEmail, editTextPassword, editTextPassword2, editTextName;
    private ProgressBar loadingProgress;
    private FirebaseAuth mAuth;
    private Button signUpBtn;
    private DatabaseReference dStorage;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        loadingProgress = findViewById(R.id.progressBar);
        loadingProgress.setVisibility(View.INVISIBLE);

        /*
        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission(RegisterActivity.this);
                } else {
                    openGallery();
                }
            }
        });*/

        editTextEmail = findViewById(R.id.regMail);
        editTextPassword = findViewById(R.id.regPassword);
        editTextPassword2 = findViewById(R.id.regPassword2);
        editTextName = findViewById(R.id.regName);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etsEmail);
        etPassword = findViewById(R.id.etsPassword);
        etPassword2 = findViewById(R.id.etPassword2);
        etName.addTextChangedListener(new MyTextWatcher(etName));


        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getEditText().getText().toString().trim();
                final String email = editTextEmail.getEditText().getText().toString().trim();
                String password = editTextPassword.getEditText().getText().toString().trim();
                final String password2 = editTextPassword2.getEditText().getText().toString().trim();

                signUpBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);

                if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()
                        || password.length() < 6 || password2.length() < 6 || password2.isEmpty() || !password.equals(password2) ) {
                    if (name.isEmpty()) {
                        editTextName.setError("Name is required!");
                    } else { editTextEmail.setError(null); }
                    if (email.isEmpty()) {
                        editTextEmail.setError("Email is required!");
                    } else { editTextEmail.setError(null); }
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        editTextEmail.setError("Please enter a valid email.");
                    } else { editTextEmail.setError(null); }
                    if (password.isEmpty()) {
                        editTextPassword.setError("Password cannot be empty!");
                    } else { editTextPassword.setError(null); }
                    if (password.length() < 6) {
                        editTextPassword.setError("Minimum password length is 6!");
                    } else { editTextPassword.setError(null); }
                    if (password2.isEmpty()) {
                        editTextPassword2.setError("Password cannot be empty!");
                    } else { editTextPassword2.setError(null); }
                    if (!password.equals(password2)) {
                        editTextPassword2.setError("Passwords do not match");
                    } else { editTextPassword2.setError(null); }
                } else {
                    signUpBtn.setVisibility(View.INVISIBLE);
                    loadingProgress.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), "email- " + email + " password " + password2, Toast.LENGTH_SHORT).show();
                    mAuth.createUserWithEmailAndPassword(email, password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                Toast.makeText(RegisterActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                                updateUserInfo(editTextName.getEditText(), mAuth.getCurrentUser());
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(RegisterActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    signUpBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cR.getType(uri));
    }

    //update user photo and name
    private void updateUserInfo(final EditText editTextName, final FirebaseUser currentUser) {
        //To upload user photo to Firebase Storage in "users_photo" node
        //mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");

        //To store the names of the users to Firebase Database in "users_names" node
        dStorage = FirebaseDatabase.getInstance().getReference().child("users_names");

        //to give the name to the user photos (tag with unique key to retrieve data)
        /*final StorageReference fileReference;
        if (getFileExtension(pickedImgUri) == null) {
            fileReference = mStorage.child(System.currentTimeMillis() + ".png");
        } else {
            fileReference = mStorage.child(System.currentTimeMillis() + "." + getFileExtension(pickedImgUri));
        }

        //upload to Firebase servers
        fileReference.putFile(pickedImgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingProgress.setProgress(0);
                            }
                        }, 5000);
                        fileReference.putFile(pickedImgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                imageLink = fileReference.getDownloadUrl().toString();
                                return fileReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    imageLink = task.getResult().toString();
                                    Toast.makeText(RegisterActivity.this, "got the Product image Url Successfully... link is - " + imageLink, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        User user = new User(currentUser.getUid(), "https://firebasestorage.googleapis.com/v0/b/blogapp-72a37.appspot.com/o/users_photos%2F1559635064314.null?alt=media&token=8c25427f-bd7f-49b9-8d70-4ac396cac0af",
                                etName.getText().toString(), etEmail.getText().toString(), "");

                        //firebase generates unique key(how to retrieve afterwards)
                        dStorage.child(mAuth.getCurrentUser().getUid()).setValue(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage() + "from database", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        loadingProgress.setProgress((int) progress);
                    }
                });
        */
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());
        User user = new User(currentUser.getUid(), "https://firebasestorage.googleapis.com/v0/b/blogapp-72a37.appspot.com/o/users_photos%2F1559635064314.null?alt=media&token=8c25427f-bd7f-49b9-8d70-4ac396cac0af",
                etName.getText().toString(), etEmail.getText().toString(), "", date);

        //firebase generates unique key(how to retrieve afterwards)
        dStorage.child(mAuth.getCurrentUser().getUid()).setValue(user);
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }


    /*
    private void checkAndRequestForPermission(final Context context) {
        /*
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this, "Please accept the required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqcode);
            }
        } else {
            openGallery();
        }
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                ActivityCompat
                        .requestPermissions((Activity) context, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, PReqcode);
            }
        } else {
            openGallery();
        }
    }*/

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PReqcode:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(RegisterActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context, new String[] { permission }, PReqcode);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE); //to identify the image
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null && data.getData() != null) {
            pickedImgUri = data.getData();
            //pass the image back into the image
            ImgUserPhoto.setImageURI(pickedImgUri);
        }
    }*/

    private class MyTextWatcher implements TextWatcher {

        View view;

        private MyTextWatcher(View view) {
            Log.i(TAG, "MyTextWatcher Constructor: fired");
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "beforeTextChanged: fired");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.i(TAG, "onTextChanged: fired");

        }

        @Override
        public void afterTextChanged(Editable editable) {
            Log.i(TAG, "afterTextChanged: fired");
            /*
            switch (view.getId()) {

                case R.id.etName:
                    validateName();
                    break;
                case R.id.etPassword:
                    validatePassword();
                    break;
                case R.id.etEmail:
                    validateEmail();
                    break;
            }
            */
        }
    }
}
