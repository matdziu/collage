package com.collage.gallery;

import com.collage.util.models.Photo;

import java.util.List;

public interface GalleryListener {

    void onPhotosFetchingStarted();

    void onPhotosFetchingFinished(List<Photo> photosList);
}