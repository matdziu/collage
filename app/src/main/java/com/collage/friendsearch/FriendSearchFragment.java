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
import android.widget.Toast;

import com.collage.R;
import com.collage.base.BaseFragment;
import com.collage.interactors.FirebaseDatabaseInteractor;
import com.collage.util.PendingInvitationsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendSearchFragment extends BaseFragment implements FriendSearchResultListener {

    @BindView(R.id.pending_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.edit_text_friend_search)
    EditText editText;

    private FriendSearchPresenter friendSearchPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendSearchPresenter = new FriendSearchPresenter(new FirebaseDatabaseInteractor(this));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_search, container, false);
        ButterKnife.bind(this, view);

        List<String> pendingList = new ArrayList<>();
        pendingList.add("Marcin");
        pendingList.add("Marek");
        pendingList.add("Maciek");
        pendingList.add("Ania");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        layoutManager.getOrientation());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(new PendingInvitationsAdapter(pendingList));

        return view;
    }

    @Override
    public void onFriendFound() {
        Toast.makeText(getContext(), "Invitation sent", Toast.LENGTH_SHORT).show();
        editText.setText("");
    }

    @Override
    public void onFriendNotFound() {
        Toast.makeText(getContext(), "Friend not found", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_friend_search)
    public void onInviteButtonClicked() {
        friendSearchPresenter.searchForFriend(
                editText.getText()
                        .toString()
                        .toLowerCase()
                        .trim());
    }
}
