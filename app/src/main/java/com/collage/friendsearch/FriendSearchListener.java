package com.collage.friendsearch;


import com.collage.base.BaseUsersListener;
import com.collage.util.model.User;

import java.util.List;

public interface FriendSearchListener extends BaseUsersListener {

    void onFriendFound();

    void onFriendNotFound();

    void onInvitationAccepted(int position, User friend);
}
