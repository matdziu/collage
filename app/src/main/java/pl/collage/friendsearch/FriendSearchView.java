package pl.collage.friendsearch;

import pl.collage.base.BaseView;
import pl.collage.util.models.User;

interface FriendSearchView extends BaseView<User> {

    void showFriendFound();

    void showFriendNotFound();

    void removeFromRecyclerView(int position);
}
