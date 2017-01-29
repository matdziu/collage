package com.collage.friends;

import android.content.Intent;
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
import com.collage.friendsearch.FriendSearchActivity;
import com.collage.home.HomeActivity;
import com.collage.util.adapters.FriendsAdapter;
import com.collage.util.events.GalleryEvent;
import com.collage.util.interactors.FirebaseDatabaseInteractor;
import com.collage.util.models.User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendsFragment extends BaseFragment implements FriendsView {

    @BindView(R.id.friends_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FriendsPresenter friendsPresenter;
    private HomeActivity homeActivity;
    private FriendsAdapter friendsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsPresenter = new FriendsPresenter(this, new FirebaseDatabaseInteractor());
        friendsAdapter = new FriendsAdapter(new ArrayList<User>(), friendsPresenter, getContext());
        homeActivity = (HomeActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(friendsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        friendsPresenter.populateFriendsList();
    }

    @Override
    public void setMenuVisibility(boolean fragmentVisible) {
        super.setMenuVisibility(fragmentVisible);
        if (homeActivity != null) {
            if (fragmentVisible) {
                showSystemUI();
                homeActivity.showHomeNavigation();
            }
        }
    }

    @OnClick(R.id.fab_add_friend)
    public void onAddFriendClicked() {
        startActivity(new Intent(getActivity(), FriendSearchActivity.class));
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
    public void updateRecyclerView(List<User> usersList) {
        friendsAdapter.setFriendList(usersList);
    }

    @Override
    public void navigateToGalleryFragment() {
        homeActivity.navigateToGalleryFragment(1);
    }

    @Override
    public void postGalleryEvent(GalleryEvent galleryEvent) {
        EventBus.getDefault().post(galleryEvent);
    }
}
