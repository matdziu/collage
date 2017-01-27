package com.collage.base;

import com.collage.util.model.User;

import java.util.List;

public interface BaseFriendsListener {

    void onFriendsListFetchingStarted();

    void onFriendsListFetched(List<User> friendsList);
}
