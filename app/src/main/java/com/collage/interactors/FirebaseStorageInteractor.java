package com.collage.interactors;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class FirebaseStorageInteractor {

    private StorageReference storageReference =
            FirebaseStorage.getInstance().getReference();

    public void uploadImage(String albumStorageId, String imageFilePath,
                            String imageFileName) {
        Uri imageFile = Uri.fromFile(new File(imageFilePath));
        storageReference.child(albumStorageId)
                .child(imageFileName)
                .putFile(imageFile);
    }
}
