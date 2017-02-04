package com.collage.sendimage;

import android.net.Uri;

import com.collage.base.BasePresenter;
import com.collage.util.interactors.FirebaseDatabaseInteractor;
import com.collage.util.interactors.FirebaseStorageInteractor;
import com.collage.util.models.User;

import java.util.List;

class SendImagePresenter extends BasePresenter implements SendImageListener {

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
        this.usersList = friendsList;
        sendImageView.hideProgressBar();
        sendImageView.updateRecyclerView(friendsList);
    }

    @Override
    public void onImageUploadStarted(User friend) {
        firebaseStorageInteractor.uploadImage(friend, imageFilePath,
                imageFileName, this);
    }

    @Override
    public void onImageUploadFinished(Uri downloadUrl, User friendFinished) {
        List<User> updatedList = filteredList != null ? filteredList : usersList;
        for (int position = 0; position < updatedList.size(); position++) {
            if (updatedList.get(position).uid
                    .equals(friendFinished.uid)) {
                updatedList.set(position, friendFinished);
            }
        }
        sendImageView.updateRecyclerView(updatedList);
        sendImageView.setPictureSentResult();
        firebaseDatabaseInteractor.addImageUrl(downloadUrl, friendFinished);
    }
}
