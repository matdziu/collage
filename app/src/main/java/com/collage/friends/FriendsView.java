package com.collage.friends;

import com.collage.base.BaseView;
import com.collage.util.events.GalleryEvent;
import com.collage.util.models.User;

interface FriendsView extends BaseView<User> {

    void navigateToGalleryFragment();

    void postGalleryEvent(GalleryEvent galleryEvent);
}
