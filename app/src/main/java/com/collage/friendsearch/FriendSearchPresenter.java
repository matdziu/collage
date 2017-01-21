package com.collage.friendsearch;


import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.util.model.User;

import java.util.List;

class FriendSearchPresenter {

    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendSearchPresenter(FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void searchForFriend(String email) {
        firebaseDatabaseInteractor.searchForFriend(email);
    }

    void populatePendingList(List<User> pendingList) {
        firebaseDatabaseInteractor.fetchPendingList(pendingList);
    }

    void addFriend(User friend) {
        firebaseDatabaseInteractor.addFriend(friend);
    }
}
