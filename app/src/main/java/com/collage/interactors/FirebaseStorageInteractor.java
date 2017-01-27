package com.collage.interactors;

import android.net.Uri;

import com.collage.sendimage.SendImageListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FirebaseStorageInteractor {

    private StorageReference storageReference =
            FirebaseStorage.getInstance().getReference();

    public void uploadImage(String albumStorageId, String imageFilePath,
                            String imageFileName, final int position,
                            final SendImageListener sendImageListener) {
        Uri imageFile = Uri.fromFile(new File(imageFilePath));
        storageReference.child(albumStorageId)
                .child(imageFileName)
                .putFile(imageFile)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        sendImageListener.onImageUploadFinished(position);
                    }
                });
    }
}
