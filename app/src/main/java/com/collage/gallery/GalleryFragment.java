package com.collage.gallery;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.collage.R;
import com.collage.base.BaseFragment;
import com.collage.home.HomeActivity;
import com.collage.util.adapters.PhotosAdapter;
import com.collage.util.events.GalleryEvent;
import com.collage.util.model.Photo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends BaseFragment {

    @BindView(R.id.gallery_recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);

        List<Photo> photoList = new ArrayList<>();

        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo), getProperWidth()));

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new PhotosAdapter(photoList));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGalleryEvent(GalleryEvent galleryEvent) {
        Toast.makeText(getContext(), galleryEvent.getAlbumStorageId(), Toast.LENGTH_SHORT).show();
    }

    private int getProperWidth() {
        Display display = getActivity().
                getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return (size.x / 3);
    }

    @Override
    public void setMenuVisibility(boolean fragmentVisible) {
        super.setMenuVisibility(fragmentVisible);
        HomeActivity homeActivity = (HomeActivity) getActivity();
        if (homeActivity != null) {
            if (fragmentVisible) {
                showSystemUI();
                homeActivity.showHomeNavigation();
            }
        }
    }
}
