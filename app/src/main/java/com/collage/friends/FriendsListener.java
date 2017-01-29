package com.collage.friends;

import com.collage.base.BaseUsersListener;

public interface FriendsListener extends BaseUsersListener {

    void onFriendSelected(String albumStorageId);
}
