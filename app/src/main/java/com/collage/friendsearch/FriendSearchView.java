package com.collage.friendsearch;

import com.collage.base.BaseView;
import com.collage.util.models.User;

interface FriendSearchView extends BaseView<User> {

    void showFriendFound();

    void showFriendNotFound();

    void removeFromRecyclerView(int position);
}
