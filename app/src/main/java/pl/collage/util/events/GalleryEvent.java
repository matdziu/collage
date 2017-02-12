package pl.collage.util.events;

import pl.collage.util.models.User;

public class GalleryEvent {

    private User friend;

    public GalleryEvent(User friend) {
        this.friend = friend;
    }

    public User getFriend() {
        return friend;
    }
}
