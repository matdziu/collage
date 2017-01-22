package com.collage.friends;

import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.util.model.User;

import java.util.List;

class FriendsPresenter implements FriendsListener {

    private FriendsView friendsView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendsPresenter(FriendsView friendsView, FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.friendsView = friendsView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void populateFriendsList() {
        firebaseDatabaseInteractor.fetchFriendsList(this);
    }

    @Override
    public void onFriendsListFetchingStarted() {
        friendsView.showProgressBar();
    }

    @Override
    public void onFriendsListFetched(List<User> friendsList) {
        friendsView.hideProgressBar();
        friendsView.updateRecyclerView(friendsList);
    }
}
