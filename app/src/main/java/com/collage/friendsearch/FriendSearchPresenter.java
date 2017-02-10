package com.collage.friendsearch;

import com.collage.base.BasePresenter;
import com.collage.util.interactors.FirebaseDatabaseInteractor;
import com.collage.util.models.User;

import java.util.List;

class FriendSearchPresenter extends BasePresenter implements FriendSearchListener {

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
        if (friendSearchView.isConnected()) firebaseDatabaseInteractor.fetchPendingList(this);
        else friendSearchView.showConnectionError();
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

        if (filteredList != null) {
            filteredList.remove(friend);
        }
        usersList.remove(friend);

        firebaseDatabaseInteractor.addFriend(friend);
    }

    @Override
    public void onListFetchingStarted() {
        friendSearchView.showProgressBar();
    }

    @Override
    public void onListFetched(List<User> pendingList) {
        this.usersList = pendingList;
        friendSearchView.hideProgressBar();
        friendSearchView.updateRecyclerView(pendingList);
    }
}
