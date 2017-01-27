package com.collage.sendimage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.collage.R;
import com.collage.base.BaseFragment;
import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.interactors.FirebaseStorageInteractor;
import com.collage.util.adapters.SendImageAdapter;
import com.collage.util.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.collage.camera.CameraFragment.IMAGE_FILE_NAME;
import static com.collage.camera.CameraFragment.IMAGE_FILE_PATH;

public class SendImageFragment extends BaseFragment implements SendImageView {

    private SendImagePresenter sendImagePresenter;

    @BindView(R.id.send_image_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendImagePresenter = new SendImagePresenter(this, new FirebaseDatabaseInteractor(),
                new FirebaseStorageInteractor(), getArguments().getString(IMAGE_FILE_PATH),
                getArguments().getString(IMAGE_FILE_NAME));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_image, container, false);
        ButterKnife.bind(this, view);

        sendImagePresenter.populatePendingList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
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
    public void showItemProgressBar(int position) {

    }

    @Override
    public void hideItemProgressBar(int position) {

    }

    @Override
    public void updateRecyclerView(List<User> friendsList) {
        recyclerView.setAdapter(new SendImageAdapter(friendsList, sendImagePresenter));
    }
}
