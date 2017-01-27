package com.collage.sendimage;

import com.collage.base.BaseUsersListener;

public interface SendImageListener extends BaseUsersListener {

    void onSendButtonClicked(String albumStorageId);
}
