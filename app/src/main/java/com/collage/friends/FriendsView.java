package com.collage.friends;

import com.collage.base.BaseUsersView;
import com.collage.util.events.GalleryEvent;
import com.collage.util.models.User;

interface FriendsView extends BaseUsersView {

    void showProgressBar();

    void hideProgressBar();

    void navigateToGalleryFragment();

    void postGalleryEvent(GalleryEvent galleryEvent);
}
