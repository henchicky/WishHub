package com.example.wishhub.HomePage;

import android.net.Uri;

import java.net.URI;

public class ImageUri {

    private Uri imageUri;

    public ImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
