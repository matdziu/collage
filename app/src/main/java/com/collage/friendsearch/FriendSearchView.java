package com.collage.friendsearch;

import com.collage.base.BaseUsersView;
import com.collage.util.models.User;

interface FriendSearchView extends BaseUsersView {

    void showProgressBar();

    void hideProgressBar();

    void showFriendFound();

    void showFriendNotFound();

    void removeFromRecyclerView(int position);
}
