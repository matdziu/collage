package com.collage.gallerydetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.collage.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.collage.util.adapters.PhotosDetailAdapter.EXTRAS_PHOTO_URL;

public class GalleryDetailFragment extends Fragment {

    @BindView(R.id.gallery_detail_image_view)
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String photoUrl = getArguments().getString(EXTRAS_PHOTO_URL);
        Glide.with(this)
                .load(photoUrl)
                .into(imageView);
    }
}
