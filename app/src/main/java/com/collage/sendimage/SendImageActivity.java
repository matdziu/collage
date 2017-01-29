package com.collage.sendimage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.collage.R;
import com.collage.base.BaseActivity;

public class SendImageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment sendImageFragment = new SendImageFragment();
        Bundle arguments = getIntent().getExtras();
        sendImageFragment.setArguments(arguments);

        transaction.replace(R.id.layout_activity_send_image, sendImageFragment);
        transaction.commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_send_image;
    }
}
