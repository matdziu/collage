package com.collage.friendsearch;


import com.collage.interactors.FirebaseDatabaseInteractor;

import java.util.List;

class FriendSearchPresenter {

    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendSearchPresenter(FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void searchForFriend(String email) {
        firebaseDatabaseInteractor.searchForFriend(email);
    }

    void populatePendingList(List<String> pendingList) {
        firebaseDatabaseInteractor.fetchPendingList(pendingList);
    }
}
