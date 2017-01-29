package com.collage.util.events;

import com.collage.util.models.User;

public class GalleryEvent {

    private User friend;

    public GalleryEvent(User friend) {
        this.friend = friend;
    }

    public User getFriend() {
        return friend;
    }
}
