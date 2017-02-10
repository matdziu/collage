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
        if (sendImageView.isConnected()) firebaseDatabaseInteractor.fetchFriendsList(this);
        else sendImageView.showConnectionError();
    }

    @Override
    public void onListFetchingStarted() {
        sendImageView.showProgressBar();
    }

    @Override
    public void onListFetched(List<User> friendsList) {
        this.usersList = friendsList;
        sendImageView.hideProgressBar();
        sendImageView.updateRecyclerView(friendsList);
        if (friendsList.size() > 0) sendImageView.hideNoItemsInfo();
        else sendImageView.showNoItemsInfo();
    }

    @Override
    public void onImageUploadStarted(User friendStarted) {
        sendImageView.updateRecyclerView(updateList(friendStarted));
        firebaseStorageInteractor.uploadImage(friendStarted, imageFilePath,
                imageFileName, this);
    }

    @Override
    public void onImageUploadFinished(Uri downloadUrl, User friendFinished) {
        sendImageView.updateRecyclerView(updateList(friendFinished));
        sendImageView.setPictureSentResult();
        firebaseDatabaseInteractor.addImageUrl(downloadUrl, friendFinished);
    }

    private List<User> updateList(User friend) {
        List<User> updatedList = filteredList != null ? filteredList : usersList;
        for (int position = 0; position < updatedList.size(); position++) {
            if (updatedList.get(position).uid
                    .equals(friend.uid)) {
                updatedList.set(position, friend);
            }
        }
        return updatedList;
    }
}
