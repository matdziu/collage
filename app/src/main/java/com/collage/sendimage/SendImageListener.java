package com.collage.sendimage;

import android.net.Uri;

import com.collage.base.BaseListener;
import com.collage.util.models.User;

public interface SendImageListener extends BaseListener<User> {

    void onImageUploadStarted(User friendStarted);

    void onImageUploadFinished(Uri downloadUrl, User friendFinished);
}
