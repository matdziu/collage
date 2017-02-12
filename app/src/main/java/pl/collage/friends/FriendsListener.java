package pl.collage.friends;

import pl.collage.base.BaseListener;
import pl.collage.util.models.User;

public interface FriendsListener extends BaseListener<User> {

    void onFriendSelected(User friend);
}
