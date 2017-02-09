package com.collage.friends;

import com.collage.base.BasePresenter;
import com.collage.util.events.GalleryEvent;
import com.collage.util.interactors.FirebaseDatabaseInteractor;
import com.collage.util.models.User;

import java.util.List;

class FriendsPresenter extends BasePresenter implements FriendsListener {

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
    public void onUsersListFetchingStarted() {
        friendsView.showProgressBar();
    }

    @Override
    public void onUsersListFetched(List<User> friendsList) {
        this.usersList = friendsList;
        friendsView.hideProgressBar();
        friendsView.updateRecyclerView(friendsList);
    }

    @Override
    public void onConnectionError() {
        friendsView.showConnectionError();
    }

    @Override
    public void onFriendSelected(User friend) {
        friendsView.navigateToGalleryFragment();
        friendsView.postGalleryEvent(new GalleryEvent(friend));
    }
}
