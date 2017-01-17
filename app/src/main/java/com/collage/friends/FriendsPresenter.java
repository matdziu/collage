package com.collage.friends;


import com.collage.interactors.FirebaseDatabaseInteractor;

class FriendsPresenter {

    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendsPresenter(FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void searchForFriend(String email) {
        firebaseDatabaseInteractor.searchForFriend(email);
    }
}
