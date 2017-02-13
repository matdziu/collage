package pl.collage.gallery;

import pl.collage.base.BaseListener;
import pl.collage.util.models.Photo;
import pl.collage.util.models.User;

public interface GalleryListener extends BaseListener<Photo> {

    void onPhotoRemovalStarted(String imageId);

    void removePhotoFromStorage(String imageId, User currentFriend);

    void onPhotoRemovalFinished();
}