package com.collage.friends;

import com.collage.util.model.User;

import java.util.List;

public interface FriendsListener {

    void onFriendsListFetchingStarted();

    void onFriendsListFetched(List<User> friendsList);
}
