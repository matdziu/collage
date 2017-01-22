package com.collage.friends;

import com.collage.util.model.User;

import java.util.List;

interface FriendsView {

    void showProgressBar();

    void hideProgressBar();

    void updateRecyclerView(List<User> friendsList);
}
