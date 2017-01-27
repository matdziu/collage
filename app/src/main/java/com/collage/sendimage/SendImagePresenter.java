package com.collage.sendimage;


import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.util.model.User;

import java.util.List;

class SendImagePresenter implements SendImageListener {

    private SendImageView sendImageView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    SendImagePresenter(SendImageView sendImageView,
                       FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
        this.sendImageView = sendImageView;
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
