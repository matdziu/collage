package com.collage.base;

import com.collage.util.models.User;

import java.util.List;

public interface BaseUsersListener {

    void onUsersListFetchingStarted();

    void onUsersListFetched(List<User> usersList);
}
