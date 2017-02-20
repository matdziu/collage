package pl.collage.util.events;

import pl.collage.util.models.User;

public class FriendSelectedEvent {

    private User friend;

    public FriendSelectedEvent(User friend) {
        this.friend = friend;
    }

    public User getFriend() {
        return friend;
    }
}
