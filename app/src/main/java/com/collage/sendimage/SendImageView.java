package com.collage.sendimage;

import com.collage.base.BaseUsersView;

interface SendImageView extends BaseUsersView {

    void showProgressBar();

    void hideProgressBar();

    void setPictureSentResult();
}
