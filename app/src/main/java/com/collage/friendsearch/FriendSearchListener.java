package com.collage.friendsearch;

import com.collage.base.BaseListener;
import com.collage.util.models.User;

public interface FriendSearchListener extends BaseListener<User> {

    void onFriendFound();

    void onFriendNotFound();

    void onInvitationAccepted(int position, User friend);
}
