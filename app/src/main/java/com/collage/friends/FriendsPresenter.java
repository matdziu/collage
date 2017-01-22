package com.collage.friends;

import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.util.model.User;

import java.util.List;

class FriendsPresenter {

    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendsPresenter(FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void populateFriendsList(List<User> friendsList) {
        firebaseDatabaseInteractor.fetchFriendsList(friendsList);
    }
}
