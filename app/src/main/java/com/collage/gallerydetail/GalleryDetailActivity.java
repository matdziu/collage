package com.collage.gallerydetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.collage.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryDetailActivity extends AppCompatActivity {

    @BindView(R.id.gallery_detail_view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_gallery_detail);
    }
}
