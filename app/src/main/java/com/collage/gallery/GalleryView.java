package com.collage.gallery;

import com.collage.util.models.Photo;

import java.util.List;

interface GalleryView {

    void showProgressBar();

    void hideProgressBar();

    void updateRecyclerView(List<Photo> photosList);
}
