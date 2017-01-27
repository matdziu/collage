package com.collage.sendimage;

import com.collage.base.BaseUsersListener;

public interface SendImageListener extends BaseUsersListener {

    void onImageUploadStarted(String albumStorageId, int position);

    void onImageUploadFinished(int position);
}
