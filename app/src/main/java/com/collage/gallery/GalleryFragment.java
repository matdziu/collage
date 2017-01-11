package com.collage.gallery;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collage.R;
import com.collage.util.model.Photo;
import com.collage.util.PhotosAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends Fragment {

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
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private View getDecorView() {
        return getActivity()
                .getWindow()
                .getDecorView();
    }

    private void hideSystemUI() {
        getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private int getProperWidth() {
        Display display = getActivity().
                getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return (size.x / 3);
    }

}
