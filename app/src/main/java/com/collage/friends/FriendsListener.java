package com.collage.friends;

import com.collage.base.BaseListener;
import com.collage.util.models.User;

public interface FriendsListener extends BaseListener<User> {

    void onFriendSelected(User friend);
}
