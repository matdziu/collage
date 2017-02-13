package pl.collage.util.events;

import pl.collage.util.models.User;

public class FriendDeletionEvent {

    private User deletedFriend;

    public FriendDeletionEvent(User deletedFriend) {
        this.deletedFriend = deletedFriend;
    }

    public User getDeletedFriend() {
        return deletedFriend;
    }
}
