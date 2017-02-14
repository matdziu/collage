package pl.collage.friendsearch;

import pl.collage.base.BaseListener;
import pl.collage.util.models.User;

public interface FriendSearchListener extends BaseListener<User> {

    void onFriendFound();

    void onFriendNotFound();

    void onInvitationAccepted(int position, User friend);

    void onCantInviteYourself();

    void onAlreadyYourFriend();

    void onAlreadyInvited();
}
