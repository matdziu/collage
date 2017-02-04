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
import com.collage.util.adapters.SendImageAdapter;
import com.collage.util.interactors.FirebaseDatabaseInteractor;
import com.collage.util.interactors.FirebaseStorageInteractor;
import com.collage.util.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendImageFragment extends BaseFragment implements SendImageView {

    private SendImagePresenter sendImagePresenter;
    private SendImageAdapter sendImageAdapter;

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
        sendImageAdapter = new SendImageAdapter(new ArrayList<User>(), sendImagePresenter);

        baseUsersView = this;
        basePresenter = sendImagePresenter;
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
        recyclerView.setAdapter(sendImageAdapter);

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
    public void updateRecyclerView(List<User> friendsList) {
        recyclerView.setAdapter(new SendImageAdapter(friendsList, sendImagePresenter));
    }

    @Override
    public void setPictureSentResult() {
        getActivity().setResult(RESULT_PICTURE_SENT);
    }
}
