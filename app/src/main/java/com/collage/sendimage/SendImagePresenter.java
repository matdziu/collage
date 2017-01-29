package com.collage.sendimage;

import android.net.Uri;

import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.interactors.FirebaseStorageInteractor;
import com.collage.util.models.User;

import java.util.List;

class SendImagePresenter implements SendImageListener {

    private SendImageView sendImageView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;
    private FirebaseStorageInteractor firebaseStorageInteractor;
    private String imageFilePath;
    private String imageFileName;

    SendImagePresenter(SendImageView sendImageView,
                       FirebaseDatabaseInteractor firebaseDatabaseInteractor,
                       FirebaseStorageInteractor firebaseStorageInteractor,
                       String imageFilePath, String imageFileName) {
        this.sendImageView = sendImageView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
        this.firebaseStorageInteractor = firebaseStorageInteractor;
        this.imageFilePath = imageFilePath;
        this.imageFileName = imageFileName;
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
    public void onImageUploadStarted(User friend, int position) {
        sendImageView.showItemProgressBar(position);
        firebaseStorageInteractor.uploadImage(friend, imageFilePath,
                imageFileName, position, this);
    }

    @Override
    public void onImageUploadFinished(int position, Uri downloadUrl, User friend) {
        sendImageView.hideItemProgressBar(position);
        sendImageView.setPictureSentResult();
        firebaseDatabaseInteractor.addImageUrl(downloadUrl, friend);
    }
}
