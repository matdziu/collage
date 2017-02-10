package com.collage.friendsearch;

import com.collage.base.BaseUsersView;

interface FriendSearchView extends BaseUsersView {

    void showFriendFound();

    void showFriendNotFound();

    void removeFromRecyclerView(int position);
}
