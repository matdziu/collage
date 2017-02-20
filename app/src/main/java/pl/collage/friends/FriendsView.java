package pl.collage.friends;

import pl.collage.base.BaseView;
import pl.collage.util.events.FriendDeletionEvent;
import pl.collage.util.events.FriendSelectedEvent;
import pl.collage.util.models.User;

interface FriendsView extends BaseView<User> {

    void navigateToGalleryFragment();

    void postFriendSelectedEvent(FriendSelectedEvent friendSelectedEvent);

    void postFriendDeletionEvent(FriendDeletionEvent friendDeletionEvent);
}
