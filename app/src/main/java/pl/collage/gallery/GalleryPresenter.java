package pl.collage.gallery;

import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.models.Photo;
import pl.collage.util.models.User;

import java.util.List;

class GalleryPresenter implements GalleryListener {

    private GalleryView galleryView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    GalleryPresenter(GalleryView galleryView,
                     FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.galleryView = galleryView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void populatePhotosList(User friend) {
        if (galleryView.isConnected()) firebaseDatabaseInteractor.fetchPhotos(friend, this);
        else galleryView.showConnectionError();
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
}
