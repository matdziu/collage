package com.collage.friendsearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.collage.R;
import com.collage.base.BaseFragment;
import com.collage.util.interactors.FirebaseDatabaseInteractor;
import com.collage.util.adapters.PendingInvitationsAdapter;
import com.collage.util.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendSearchFragment extends BaseFragment implements FriendSearchView {

    @BindView(R.id.pending_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.edit_text_friend_search)
    EditText editText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FriendSearchPresenter friendSearchPresenter;
    private List<User> pendingList;
    private PendingInvitationsAdapter pendingInvitationsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendSearchPresenter = new FriendSearchPresenter(this, new FirebaseDatabaseInteractor());
        pendingInvitationsAdapter = new PendingInvitationsAdapter(new ArrayList<User>(), friendSearchPresenter);

        baseUsersView = this;
        basePresenter = friendSearchPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_search, container, false);
        ButterKnife.bind(this, view);

        friendSearchPresenter.populatePendingList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(pendingInvitationsAdapter);

        return view;
    }

    @OnClick(R.id.button_friend_search)
    public void onInviteButtonClicked() {
        friendSearchPresenter.searchForFriend(
                editText.getText()
                        .toString()
                        .toLowerCase()
                        .trim());
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
    public void updateRecyclerView(List<User> pendingList) {
        this.pendingList = pendingList;
        pendingInvitationsAdapter.setPendingInvitationsList(pendingList);
    }

    @Override
    public void showFriendFound() {
        Toast.makeText(getContext(), "Invitation sent", Toast.LENGTH_SHORT).show();
        editText.setText("");
    }

    @Override
    public void showFriendNotFound() {
        Toast.makeText(getContext(), "Friend not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeFromRecyclerView(int position) {
        pendingList.remove(position);
        recyclerView.getAdapter().notifyItemRemoved(position);
    }
}
