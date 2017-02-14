package pl.collage.friendsearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.collage.R;
import pl.collage.base.BaseFragment;
import pl.collage.util.adapters.PendingInvitationsAdapter;
import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.models.User;

public class FriendSearchFragment extends BaseFragment implements FriendSearchView {

    @BindView(R.id.pending_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.text_input_layout_friend_search)
    TextInputLayout textInputLayout;

    @BindView(R.id.edit_text_friend_search)
    TextInputEditText editText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.content_fragment_friend_search)
    ViewGroup contentFragmentFriendSearch;

    @BindView(R.id.layout_connection_error)
    ViewGroup layoutConnectionError;

    @BindView(R.id.no_items_text_view)
    TextView noItemsTextView;

    @BindView(R.id.progress_bar_invite)
    ProgressBar progressBarInvite;

    @BindView(R.id.button_friend_search)
    Button friendSearchButton;

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
        noItemsTextView.setVisibility(View.GONE);
        contentFragmentFriendSearch.setVisibility(View.GONE);
        layoutConnectionError.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.button_retry)
    public void onRetryClicked() {
        contentFragmentFriendSearch.setVisibility(View.VISIBLE);
        layoutConnectionError.setVisibility(View.GONE);
        friendSearchPresenter.populatePendingList();
    }

    @Override
    public void showFriendFound() {
        Toast.makeText(getContext(), R.string.invitation_sent, Toast.LENGTH_SHORT).show();
        editText.setText("");
    }

    @Override
    public void showFriendNotFound() {
        Toast.makeText(getContext(), R.string.friend_not_found, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeFromRecyclerView(int position) {
        pendingList.remove(position);
        recyclerView.getAdapter().notifyItemRemoved(position);
    }

    @Override
    public void showInviteProgressBar() {
        friendSearchButton.setVisibility(View.GONE);
        progressBarInvite.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInviteProgresBar() {
        friendSearchButton.setVisibility(View.VISIBLE);
        progressBarInvite.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyEmailFieldError() {
        textInputLayout.setError(getString(R.string.empty_email_invite_error_text));
    }

    @Override
    public void hideEmptyEmailFieldError() {
        textInputLayout.setError(null);
    }

    @Override
    public void showCantInviteYourself() {
        Toast.makeText(getContext(), R.string.invite_yourself_error_text, Toast.LENGTH_SHORT).show();
        editText.setText("");
    }

    @Override
    public void showAlreadyYourFriend() {
        Toast.makeText(getContext(), R.string.already_friend_error_text, Toast.LENGTH_SHORT).show();
        editText.setText("");
    }

    @Override
    public void showAlreadyInvited() {
        Toast.makeText(getContext(), R.string.already_invited_error_text, Toast.LENGTH_SHORT).show();
        editText.setText("");
    }

    @Override
    public void showAlreadyOnPending() {
        Toast.makeText(getContext(), R.string.already_pending_error_text, Toast.LENGTH_SHORT).show();
        editText.setText("");
    }
}
