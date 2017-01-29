package com.collage.sendimage;


import com.collage.util.models.User;

import java.util.List;

interface SendImageView {

    void showProgressBar();

    void hideProgressBar();

    void showItemProgressBar(int position);

    void hideItemProgressBar(int position);

    void updateRecyclerView(List<User> friendsList);

    void setPictureSentResult();
}
