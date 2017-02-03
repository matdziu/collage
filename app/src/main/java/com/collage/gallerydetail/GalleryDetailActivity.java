package com.collage.gallerydetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.collage.R;
import com.collage.util.models.Photo;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.collage.util.adapters.PhotosAdapter.EXTRAS_PHOTO_LIST;

public class GalleryDetailActivity extends AppCompatActivity {

    @BindView(R.id.gallery_detail_view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);
        ButterKnife.bind(this);

        List<Photo> photoList = Parcels.unwrap(
                getIntent().getExtras().getParcelable(EXTRAS_PHOTO_LIST));
    }
}
