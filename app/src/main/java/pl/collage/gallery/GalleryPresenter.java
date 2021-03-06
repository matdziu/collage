package pl.collage.gallery;

import java.util.List;

import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.interactors.FirebaseStorageInteractor;
import pl.collage.util.models.Photo;
import pl.collage.util.models.User;

class GalleryPresenter implements GalleryListener {

    private GalleryView galleryView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;
    private FirebaseStorageInteractor firebaseStorageInteractor;
    private User currentFriend;

    GalleryPresenter(GalleryView galleryView,
                     FirebaseDatabaseInteractor firebaseDatabaseInteractor,
                     FirebaseStorageInteractor firebaseStorageInteractor) {
        this.galleryView = galleryView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
        this.firebaseStorageInteractor = firebaseStorageInteractor;
    }

    void populatePhotosList() {
        if (currentFriend != null) {
            if (galleryView.isConnected()) {
                firebaseDatabaseInteractor.fetchPhotos(currentFriend, this);
            } else {
                galleryView.showConnectionError();
            }
        }
    }

    @Override
    public void onListFetchingStarted() {
        galleryView.showProgressBar();
    }

    @Override
    public void onListFetched(List<Photo> photosList) {
        galleryView.hideProgressBar();
        galleryView.updateRecyclerView(photosList);
        if (photosList.size() > 0) galleryView.hideNoItemsInfo();
        else galleryView.showNoItemsInfo();
    }

    void setCurrentFriend(User currentFriend) {
        this.currentFriend = currentFriend;
    }

    User getCurrentFriend() {
        return currentFriend;
    }

    @Override
    public void onPhotoRemovalStarted(String imageId) {
        if (galleryView.isConnected())
            firebaseDatabaseInteractor.removePhoto(imageId, currentFriend, this);
        else galleryView.showConnectionError();
    }

    @Override
    public void onPhotoRemovalFinished() {
        populatePhotosList();
    }

    @Override
    public void removePhotoFromStorage(String imageId, User currentFriend) {
        firebaseStorageInteractor.removePhoto(imageId, currentFriend);
    }
}
