package com.collage.interactors;


import com.collage.friends.FriendsListener;
import com.collage.friendsearch.FriendSearchListener;
import com.collage.util.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FirebaseDatabaseInteractor {

    private final static String USERS = "users";
    private final static String EMAIL = "email";
    private final static String PENDING_FRIENDS = "pendingFriends";
    private final static String ACCEPTED_FRIENDS = "acceptedFriends";

    private DatabaseReference databaseReference =
            FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance()
            .getCurrentUser();

    public void createUserDatabaseEntry(String fullName, String email) {
        firebaseUser = FirebaseAuth.getInstance()
                .getCurrentUser();
        if (firebaseUser != null) {
            databaseReference
                    .child(USERS)
                    .child(firebaseUser.getUid())
                    .setValue(new User(fullName, email, firebaseUser.getUid()));
        }
    }

    public void searchForFriend(String email, final FriendSearchListener friendSearchListener) {
        Query friendQuery = databaseReference
                .child(USERS)
                .orderByChild(EMAIL)
                .equalTo(email);
        friendQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    friendSearchListener.onFriendFound();

                    final String friendUid = dataSnapshot
                            .getChildren()
                            .iterator()
                            .next()
                            .getKey();

                    databaseReference
                            .child(USERS)
                            .child(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User collageUser = dataSnapshot.getValue(User.class);
                                    databaseReference
                                            .child(USERS)
                                            .child(friendUid)
                                            .child(PENDING_FRIENDS)
                                            .child(collageUser.uid)
                                            .setValue(collageUser);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Timber.e(databaseError.getMessage());
                                }
                            });
                } else {
                    friendSearchListener.onFriendNotFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e(databaseError.getMessage());
            }
        });
    }

    public void fetchPendingList(final FriendSearchListener friendSearchListener) {
        friendSearchListener.onPendingListFetchingStarted();
        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(PENDING_FRIENDS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<User> pendingList = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            pendingList.add(dataItem.getValue(User.class));
                        }
                        friendSearchListener.onPendingListFetched(pendingList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }

    public void addFriend(final User friend) {
        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(ACCEPTED_FRIENDS)
                .child(friend.uid)
                .setValue(friend);

        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User collageUser = dataSnapshot.getValue(User.class);
                        databaseReference
                                .child(USERS)
                                .child(friend.uid)
                                .child(ACCEPTED_FRIENDS)
                                .child(firebaseUser.getUid())
                                .setValue(collageUser);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });

        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(PENDING_FRIENDS)
                .child(friend.uid)
                .removeValue();
    }

    public void fetchFriendsList(final FriendsListener friendsListener) {
        friendsListener.onFriendsListFetchingStarted();
        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(ACCEPTED_FRIENDS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<User> friendsList = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            friendsList.add(dataItem.getValue(User.class));
                        }
                        friendsListener.onFriendsListFetched(friendsList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }
}
