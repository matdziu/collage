package com.collage.gallery;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.collage.R;
import com.collage.base.BaseFragment;
import com.collage.home.HomeActivity;
import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.util.adapters.PhotosAdapter;
import com.collage.util.events.GalleryEvent;
import com.collage.util.models.Photo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends BaseFragment implements GalleryView {

    private GalleryPresenter galleryPresenter;

    @BindView(R.id.gallery_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private int properWidth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryPresenter = new GalleryPresenter(this, new FirebaseDatabaseInteractor());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        properWidth = getProperWidth();
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

    @SuppressWarnings("unused")
    @Subscribe
    public void onGalleryEvent(GalleryEvent galleryEvent) {
        galleryPresenter.populatePhotosList(galleryEvent.getFriend());
    }

    private int getProperWidth() {
        Display display = getActivity().
                getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return (size.x / 3);
    }

    @Override
    public void showProgressBar() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateRecyclerView(List<Photo> photosList) {
        recyclerView.setAdapter(new PhotosAdapter(photosList, properWidth, getContext()));
    }
}
