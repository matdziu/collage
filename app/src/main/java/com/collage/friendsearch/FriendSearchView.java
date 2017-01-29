package com.collage.friendsearch;

import com.collage.util.models.User;

import java.util.List;

interface FriendSearchView {

    void showProgressBar();

    void hideProgressBar();

    void updateRecyclerView(List<User> pendingList);

    void showFriendFound();

    void showFriendNotFound();

    void removeFromRecyclerView(int position);
}
