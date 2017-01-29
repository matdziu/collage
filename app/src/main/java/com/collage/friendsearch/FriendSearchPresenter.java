package com.collage.friendsearch;

import com.collage.util.interactors.FirebaseDatabaseInteractor;
import com.collage.util.models.User;

import java.util.List;

class FriendSearchPresenter implements FriendSearchListener {

    private FriendSearchView friendSearchView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendSearchPresenter(FriendSearchView friendSearchView,
                          FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.friendSearchView = friendSearchView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void searchForFriend(String email) {
        firebaseDatabaseInteractor.searchForFriend(email, this);
    }

    void populatePendingList() {
        firebaseDatabaseInteractor.fetchPendingList(this);
    }

    @Override
    public void onFriendFound() {
        friendSearchView.showFriendFound();
    }

    @Override
    public void onFriendNotFound() {
        friendSearchView.showFriendNotFound();
    }

    @Override
    public void onInvitationAccepted(int position, User friend) {
        friendSearchView.removeFromRecyclerView(position);
        firebaseDatabaseInteractor.addFriend(friend);
    }

    @Override
    public void onUsersListFetchingStarted() {
        friendSearchView.showProgressBar();
    }

    @Override
    public void onUsersListFetched(List<User> pendingList) {
        friendSearchView.hideProgressBar();
        friendSearchView.updateRecyclerView(pendingList);
    }
}
