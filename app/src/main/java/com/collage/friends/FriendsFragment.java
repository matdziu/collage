package com.collage.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.content_fragment_friends)
    ViewGroup contentFragmentFriends;

    @BindView(R.id.layout_connection_error)
    ViewGroup layoutConnectionError;

    private FriendsPresenter friendsPresenter;
    private HomeActivity homeActivity;
    private FriendsAdapter friendsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsPresenter = new FriendsPresenter(this, new FirebaseDatabaseInteractor());
        friendsAdapter = new FriendsAdapter(new ArrayList<User>(), friendsPresenter, getContext());
        homeActivity = (HomeActivity) getActivity();

        baseView = this;
        basePresenter = friendsPresenter;
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

        friendsPresenter.populateFriendsList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                friendsPresenter.populateFriendsList();
            }
        });

        return view;
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
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updateRecyclerView(List<User> usersList) {
        friendsAdapter.setFriendList(usersList);
    }

    @Override
    public void showConnectionError() {
        contentFragmentFriends.setVisibility(View.GONE);
        layoutConnectionError.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_retry)
    public void onRetryClicked() {
        contentFragmentFriends.setVisibility(View.VISIBLE);
        layoutConnectionError.setVisibility(View.GONE);
        friendsPresenter.populateFriendsList();
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
