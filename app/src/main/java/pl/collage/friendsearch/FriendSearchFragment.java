package pl.collage.friendsearch;

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
import android.widget.TextView;
import android.widget.Toast;

import pl.collage.base.BaseFragment;
import pl.collage.util.adapters.PendingInvitationsAdapter;
import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendSearchFragment extends BaseFragment implements FriendSearchView {

    @BindView(pl.collage.R.id.pending_recycler_view)
    RecyclerView recyclerView;

    @BindView(pl.collage.R.id.edit_text_friend_search)
    EditText editText;

    @BindView(pl.collage.R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(pl.collage.R.id.content_fragment_friend_search)
    ViewGroup contentFragmentFriendSearch;

    @BindView(pl.collage.R.id.layout_connection_error)
    ViewGroup layoutConnectionError;

    @BindView(pl.collage.R.id.no_items_text_view)
    TextView noItemsTextView;

    private FriendSearchPresenter friendSearchPresenter;
    private List<User> pendingList;
    private PendingInvitationsAdapter pendingInvitationsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendSearchPresenter = new FriendSearchPresenter(this, new FirebaseDatabaseInteractor());
        pendingInvitationsAdapter = new PendingInvitationsAdapter(new ArrayList<User>(), friendSearchPresenter);

        baseView = this;
        basePresenter = friendSearchPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(pl.collage.R.layout.fragment_friend_search, container, false);
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

    @OnClick(pl.collage.R.id.button_friend_search)
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
    public void showNoItemsInfo() {
        noItemsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoItemsInfo() {
        noItemsTextView.setVisibility(View.GONE);
    }

    @Override
    public void updateRecyclerView(List<User> pendingList) {
        this.pendingList = pendingList;
        pendingInvitationsAdapter.setPendingInvitationsList(pendingList);
    }

    @Override
    public void showConnectionError() {
        contentFragmentFriendSearch.setVisibility(View.GONE);
        layoutConnectionError.setVisibility(View.VISIBLE);
    }

    @OnClick(pl.collage.R.id.button_retry)
    public void onRetryClicked() {
        contentFragmentFriendSearch.setVisibility(View.VISIBLE);
        layoutConnectionError.setVisibility(View.GONE);
        friendSearchPresenter.populatePendingList();
    }

    @Override
    public void showFriendFound() {
        Toast.makeText(getContext(), pl.collage.R.string.invitation_sent, Toast.LENGTH_SHORT).show();
        editText.setText("");
    }

    @Override
    public void showFriendNotFound() {
        Toast.makeText(getContext(), pl.collage.R.string.friend_not_found, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeFromRecyclerView(int position) {
        pendingList.remove(position);
        recyclerView.getAdapter().notifyItemRemoved(position);
    }
}
