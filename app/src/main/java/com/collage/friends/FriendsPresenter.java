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
        if (friendsView.isConnected()) firebaseDatabaseInteractor.fetchFriendsList(this);
        else friendsView.showConnectionError();
    }

    @Override
    public void onListFetchingStarted() {
        friendsView.showProgressBar();
    }

    @Override
    public void onListFetched(List<User> friendsList) {
        this.usersList = friendsList;
        friendsView.hideProgressBar();
        friendsView.updateRecyclerView(friendsList);
    }

    @Override
    public void onFriendSelected(User friend) {
        friendsView.navigateToGalleryFragment();
        friendsView.postGalleryEvent(new GalleryEvent(friend));
    }
}
