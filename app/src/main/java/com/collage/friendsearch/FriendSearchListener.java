package com.collage.friendsearch;


import java.util.List;

public interface FriendSearchListener {

    void onFriendFound();

    void onFriendNotFound();

    void onInvitationAccepted(int position);

    void onPendingListFetchingStarted();

    void onPendingListFetched(List<String> pendingList);
}
