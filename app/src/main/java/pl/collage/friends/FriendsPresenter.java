package pl.collage.friends;

import java.util.List;

import pl.collage.base.BasePresenter;
import pl.collage.util.events.FriendDeletionEvent;
import pl.collage.util.events.FriendSelectedEvent;
import pl.collage.util.interactors.FirebaseDatabaseInteractor;
import pl.collage.util.interactors.FirebaseStorageInteractor;
import pl.collage.util.models.Photo;
import pl.collage.util.models.User;

class FriendsPresenter extends BasePresenter implements FriendsListener {

    private FriendsView friendsView;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;
    private FirebaseStorageInteractor firebaseStorageInteractor;

    FriendsPresenter(FriendsView friendsView, FirebaseDatabaseInteractor firebaseDatabaseInteractor,
                     FirebaseStorageInteractor firebaseStorageInteractor) {
        this.friendsView = friendsView;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
        this.firebaseStorageInteractor = firebaseStorageInteractor;
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
        friendsView.postFriendSelectedEvent(new FriendSelectedEvent(friend));
    }

    @Override
    public void onFriendRemovalStarted(User friend) {
        if (friendsView.isConnected()) firebaseDatabaseInteractor.removeFriend(friend, this);
        else friendsView.showConnectionError();
    }

    @Override
    public void onFriendRemovalFinished(User deletedFriend) {
        populateFriendsList();
        friendsView.postFriendDeletionEvent(new FriendDeletionEvent(deletedFriend));
    }

    @Override
    public void onPhotosToRemoveFetched(List<Photo> photosList, User friend) {
        firebaseStorageInteractor.removeAlbum(photosList, friend);
    }
}
