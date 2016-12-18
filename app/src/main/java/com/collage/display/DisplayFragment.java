package com.collage.display;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collage.R;
import com.collage.model.Photo;
import com.collage.util.PhotosAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayFragment extends Fragment {

    @BindView(R.id.photos_recycler_view)
    RecyclerView recyclerView;

    private List<Photo> photoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoList = new ArrayList<>();

        // height and width are supplied here in dp, but layoutParams in adapter take only px hence conversion
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
        photoList.add(new Photo(ContextCompat.getDrawable(getContext(), R.drawable.sample_photo),
                convertToPx(randomiseWidth())));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display, container, false);
        ButterKnife.bind(this, view);

        // for testing purposes!
        setRetainInstance(true);

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

    private int convertToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private int randomiseWidth() {
        List<Integer> acceptable = new ArrayList<>(Arrays.asList
                (80, 120, 150, 240));
        return acceptable.get(new Random().nextInt(acceptable.size()));
    }

}
