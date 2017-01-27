package com.collage.sendimage;


import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.interactors.FirebaseStorageInteractor;
import com.collage.util.model.User;

import java.util.List;

class SendImagePresenter implements SendImageListener {

    private SendImageView sendImageView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;
    private FirebaseStorageInteractor firebaseStorageInteractor;

    SendImagePresenter(SendImageView sendImageView,
                       FirebaseDatabaseInteractor firebaseDatabaseInteractor,
                       FirebaseStorageInteractor firebaseStorageInteractor) {
        this.sendImageView = sendImageView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
        this.firebaseStorageInteractor = firebaseStorageInteractor;
    }


    void populatePendingList() {
        firebaseDatabaseInteractor.fetchFriendsList(this);
    }

    @Override
    public void onUsersListFetchingStarted() {
        sendImageView.showProgressBar();
    }

    @Override
    public void onUsersListFetched(List<User> friendsList) {
        sendImageView.hideProgressBar();
        sendImageView.updateRecyclerView(friendsList);
    }

    @Override
    public void onSendButtonClicked(String albumStorageId) {

    }
}
