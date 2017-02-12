package pl.collage.gallerydetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import pl.collage.R;
import pl.collage.util.adapters.PhotosDetailAdapter;
import pl.collage.util.models.Photo;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.collage.util.adapters.PhotosAdapter;

public class GalleryDetailActivity extends AppCompatActivity {

    @BindView(R.id.gallery_detail_view_pager)
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);
        ButterKnife.bind(this);

        List<Photo> photoList = Parcels.unwrap(
                getIntent().getExtras().getParcelable(PhotosAdapter.EXTRAS_PHOTO_LIST));
        PhotosDetailAdapter photosDetailAdapter = new PhotosDetailAdapter(getSupportFragmentManager(),
                photoList);
        viewPager.setAdapter(photosDetailAdapter);
        viewPager.setCurrentItem(getIntent().getExtras().getInt(PhotosAdapter.EXTRAS_CURRENT_PHOTO_POSITION));
    }
}
