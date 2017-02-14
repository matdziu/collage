package pl.collage.friendsearch;

import java.util.List;

import pl.collage.base.BasePresenter;
import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.models.User;

class FriendSearchPresenter extends BasePresenter implements FriendSearchListener {

    private FriendSearchView friendSearchView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendSearchPresenter(FriendSearchView friendSearchView,
                          FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.friendSearchView = friendSearchView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void searchForFriend(String email) {
        friendSearchView.showInviteProgressBar();
        firebaseDatabaseInteractor.searchForFriend(email, this);
    }

    void populatePendingList() {
        if (friendSearchView.isConnected()) firebaseDatabaseInteractor.fetchPendingList(this);
        else friendSearchView.showConnectionError();
    }

    @Override
    public void onFriendFound() {
        friendSearchView.showFriendFound();
        friendSearchView.hideInviteProgresBar();
    }

    @Override
    public void onFriendNotFound() {
        friendSearchView.showFriendNotFound();
        friendSearchView.hideInviteProgresBar();
    }

    @Override
    public void onInvitationAccepted(int position, User friend) {
        friendSearchView.removeFromRecyclerView(position);

        if (filteredList != null) {
            filteredList.remove(friend);
        }
        usersList.remove(friend);

        firebaseDatabaseInteractor.addFriend(friend);
    }

    @Override
    public void onListFetchingStarted() {
        friendSearchView.showProgressBar();
    }

    @Override
    public void onListFetched(List<User> pendingList) {
        this.usersList = pendingList;
        friendSearchView.hideProgressBar();
        friendSearchView.updateRecyclerView(pendingList);
        if (pendingList.size() > 0) friendSearchView.hideNoItemsInfo();
        else friendSearchView.showNoItemsInfo();
    }
}
