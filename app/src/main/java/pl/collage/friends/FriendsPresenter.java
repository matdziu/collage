package pl.collage.friends;

import pl.collage.base.BasePresenter;
import pl.collage.util.events.GalleryEvent;
import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.models.User;

import java.util.List;

class FriendsPresenter extends BasePresenter implements FriendsListener {

    private FriendsView friendsView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;

    FriendsPresenter(FriendsView friendsView, FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.friendsView = friendsView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void populateFriendsList() {
        if (friendsView.isConnected()) firebaseDatabaseInteractor.fetchFriendsList(this);
        else friendsView.showConnectionError();
    }

    @Override
    public void onListFetchingStarted() {
        friendsView.showProgressBar();
    }

    @Override
    public void onListFetched(List<User> friendsList) {
        this.usersList = friendsList;
        friendsView.hideProgressBar();
        friendsView.updateRecyclerView(friendsList);
        if (friendsList.size() > 0) friendsView.hideNoItemsInfo();
        else friendsView.showNoItemsInfo();
    }

    @Override
    public void onFriendSelected(User friend) {
        friendsView.navigateToGalleryFragment();
        friendsView.postGalleryEvent(new GalleryEvent(friend));
    }
}
