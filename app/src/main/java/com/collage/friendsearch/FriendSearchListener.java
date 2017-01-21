package com.collage.friendsearch;


import com.collage.util.model.User;

import java.util.List;

public interface FriendSearchListener {

    void onFriendFound();

    void onFriendNotFound();

    void onInvitationAccepted(int position, User friend);

    void onPendingListFetchingStarted();

    void onPendingListFetched(List<User> pendingList);
}
