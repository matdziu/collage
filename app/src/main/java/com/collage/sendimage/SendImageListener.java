package com.collage.sendimage;

import android.net.Uri;

import com.collage.base.BaseUsersListener;
import com.collage.util.models.User;

public interface SendImageListener extends BaseUsersListener {

    void onImageUploadStarted(User friend);

    void onImageUploadFinished(Uri downloadUrl, User friendFinished);
}
