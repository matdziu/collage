package com.collage.sendimage;

import com.collage.base.BaseUsersView;
import com.collage.util.models.User;

interface SendImageView extends BaseUsersView {

    void showProgressBar();

    void hideProgressBar();

    void showItemProgressBar(int position);

    void hideItemProgressBar(int position);

    void setPictureSentResult();
}
