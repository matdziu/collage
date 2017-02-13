package pl.collage.util.interactors;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

import pl.collage.sendimage.SendImageListener;
import pl.collage.util.models.Photo;
import pl.collage.util.models.User;

public class FirebaseStorageInteractor {

    private StorageReference storageReference =
            FirebaseStorage.getInstance().getReference();

    public void uploadImage(final User friend, String imageFilePath, final String imageFileName,
                            final SendImageListener sendImageListener) {
        Uri imageFile = Uri.fromFile(new File(imageFilePath));
        storageReference.child(friend.albumStorageId)
                .child(imageFileName)
                .putFile(imageFile)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Photo uploadedPhoto = new Photo();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        if (downloadUrl != null) uploadedPhoto.imageUrl = downloadUrl.toString();
                        uploadedPhoto.imageId = imageFileName;

                        friend.sendingStarted = false;
                        friend.sendingFinished = true;
                        sendImageListener.onImageUploadFinished(uploadedPhoto, friend);
                    }
                });
    }

    public void removeAlbum(List<Photo> photoList, User friend) {
        StorageReference albumReference = storageReference.child(friend.albumStorageId);
        for (Photo photo : photoList) {
            albumReference.child(photo.imageId).delete();
        }
    }
}
