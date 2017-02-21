package pl.collage.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import pl.collage.R;
import pl.collage.base.BaseFragment;
import pl.collage.home.HomeActivity;
import pl.collage.sendimage.SendImageActivity;
import pl.collage.util.adapters.PhotosAdapter;
import pl.collage.util.events.FriendDeletionEvent;
import pl.collage.util.events.FriendSelectedEvent;
import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.interactors.FirebaseStorageInteractor;
import pl.collage.util.models.Photo;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends BaseFragment implements GalleryView {

    private GalleryPresenter galleryPresenter;

    @BindView(R.id.gallery_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.content_fragment_gallery)
    ViewGroup contentFragmentGallery;

    @BindView(R.id.layout_connection_error)
    ViewGroup layoutConnectionError;

    @BindView(R.id.no_items_text_view)
    TextView noItemsTextView;

    @BindView(R.id.no_friend_selected_text_view)
    TextView noFriendSelectedTextView;

    private PhotosAdapter photosAdapter;
    private static final int REQUEST_PICK_IMAGE = 2;
    public static final int SPAN_COUNT = 3;

    HomeActivity homeActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        galleryPresenter = new GalleryPresenter(this, new FirebaseDatabaseInteractor(), new FirebaseStorageInteractor());
        photosAdapter = new PhotosAdapter(new ArrayList<Photo>(), getScreenSize(), getContext(), galleryPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(photosAdapter);
        noFriendSelectedTextView.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
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
    public void setMenuVisibility(final boolean fragmentVisible) {
        super.setMenuVisibility(fragmentVisible);
        if (homeActivity != null) {
            ActionBar toolbar = homeActivity.getSupportActionBar();
            if (fragmentVisible && toolbar != null) {
                showSystemUI();
                homeActivity.showHomeNavigation();
                toolbar.setTitle(R.string.gallery_screen_title);
            }
        }

        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                    if (galleryPresenter.getCurrentFriend() != null
                            && fragmentVisible) {
                        homeActivity.navigateToFriendsFragment(0);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchMenuItem.setVisible(false);
    }

    @Subscribe
    public void onFriendSelectedEvent(FriendSelectedEvent friendSelectedEvent) {
        galleryPresenter.setCurrentFriend(friendSelectedEvent.getFriend());
        galleryPresenter.populatePhotosList();
        noFriendSelectedTextView.setVisibility(View.GONE);
    }

    @Subscribe
    public void onFriendDeletionEvent(FriendDeletionEvent friendDeletionEvent) {
        if (friendDeletionEvent.getDeletedFriend().uid
                .equals(galleryPresenter.getCurrentFriend().uid)) {
            noFriendSelectedTextView.setVisibility(View.VISIBLE);
            noItemsTextView.setVisibility(View.GONE);
            photosAdapter.setPhotoList(new ArrayList<Photo>());
        }
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
    public void showNoItemsInfo() {
        noItemsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoItemsInfo() {
        noItemsTextView.setVisibility(View.GONE);
    }

    @Override
    public void updateRecyclerView(List<Photo> photosList) {
        photosAdapter.setPhotoList(photosList);
    }

    @Override
    public void showConnectionError() {
        contentFragmentGallery.setVisibility(View.GONE);
        layoutConnectionError.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_retry)
    public void onRetryClicked() {
        contentFragmentGallery.setVisibility(View.VISIBLE);
        layoutConnectionError.setVisibility(View.GONE);
        galleryPresenter.populatePhotosList();
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
