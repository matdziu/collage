package com.collage.friendsearch;


import com.collage.interactors.FirebaseDatabaseInteractor;

class FriendSearchPresenter {

    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendSearchPresenter(FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void searchForFriend(String email) {
        firebaseDatabaseInteractor.searchForFriend(email);
    }
}
