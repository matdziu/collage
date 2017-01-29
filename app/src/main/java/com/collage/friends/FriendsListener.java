package com.collage.friends;

import com.collage.base.BaseUsersListener;
import com.collage.util.models.User;

public interface FriendsListener extends BaseUsersListener {

    void onFriendSelected(User friend);
}
