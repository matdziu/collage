package pl.collage.friends;

import java.util.List;

import pl.collage.base.BaseListener;
import pl.collage.util.models.Photo;
import pl.collage.util.models.User;

public interface FriendsListener extends BaseListener<User> {

    void onFriendSelected(User friend);

    void onFriendRemovalStarted(User friend);

    void onFriendRemovalFinished();

    void onPhotosToRemoveFetched(List<Photo> photosList, User friend);
}
