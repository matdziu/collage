package com.collage.gallery;

import android.content.Intent;
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
import com.collage.sendimage.SendImageActivity;
import com.collage.util.adapters.PhotosAdapter;
import com.collage.util.events.GalleryEvent;
import com.collage.util.interactors.FirebaseDatabaseInteractor;
import com.collage.util.models.Photo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends BaseFragment implements GalleryView {

    private GalleryPresenter galleryPresenter;

    @BindView(R.id.gallery_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private PhotosAdapter photosAdapter;
    private static final int REQUEST_PICK_IMAGE = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryPresenter = new GalleryPresenter(this, new FirebaseDatabaseInteractor());
        photosAdapter = new PhotosAdapter(new ArrayList<Photo>(), getProperWidth(), getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(photosAdapter);
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
        photosAdapter.setPhotoList(photosList);
    }

    @OnClick(R.id.fab_add_photo)
    public void onAddPhotoClicked() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        startActivityForResult(getIntent, REQUEST_PICK_IMAGE);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    imageFile = createImageFile(createImageGallery());
                    InputStream inputStream = getContext()
                            .getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {
                        byte[] buffer = new byte[inputStream.available()];
                        inputStream.read(buffer);

                        OutputStream outputStream = new FileOutputStream(imageFile);
                        outputStream.write(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getContext(), SendImageActivity.class);
                intent.putExtra(IMAGE_FILE_PATH, imageFile.getPath());
                intent.putExtra(IMAGE_FILE_NAME, imageFile.getName());
                startActivityForResult(intent, REQUEST_SEND_IMAGE);
            }
        }

        if (requestCode == REQUEST_SEND_IMAGE) {
            imageFile.delete();
        }
    }
}
