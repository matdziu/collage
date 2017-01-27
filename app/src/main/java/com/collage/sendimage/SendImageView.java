package com.collage.sendimage;


import com.collage.util.model.User;

import java.util.List;

interface SendImageView {

    void showProgressBar();

    void hideProgressBar();

    void updateRecyclerView(List<User> friendsList);
}
