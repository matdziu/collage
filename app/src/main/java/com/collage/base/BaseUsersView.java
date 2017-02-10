package com.collage.base;

import com.collage.util.models.User;

import java.util.List;

public interface BaseUsersView {

    void updateRecyclerView(List<User> userList);

    void showConnectionError();

    boolean isConnected();
}
