package com.collage.interactors;


import android.util.Log;

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

public class FirebaseDatabaseInteractor {

    private DatabaseReference databaseReference =
            FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance()
            .getCurrentUser();

    public void createUserDatabaseEntry(String fullName, String email) {
        firebaseUser = FirebaseAuth.getInstance()
                .getCurrentUser();
        if (firebaseUser != null) {
            databaseReference
                    .child("users")
                    .child(firebaseUser.getUid())
                    .setValue(new User(fullName, email, firebaseUser.getUid()));
        }
    }

    public void searchForFriend(String email, final FriendSearchListener friendSearchListener) {
        Query friendQuery = databaseReference
                .child("users")
                .orderByChild("email")
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
                            .child("users")
                            .child(firebaseUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User collageUser = dataSnapshot.getValue(User.class);
                                    databaseReference
                                            .child("users")
                                            .child(friendUid)
                                            .child("pendingFriends")
                                            .child(collageUser.uid)
                                            .setValue(collageUser);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("friendSearch", databaseError.getMessage());
                                }
                            });
                } else {
                    friendSearchListener.onFriendNotFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("friendSearch", databaseError.getMessage());
            }
        });
    }

    public void fetchPendingList(final FriendSearchListener friendSearchListener) {
        friendSearchListener.onPendingListFetchingStarted();
        databaseReference
                .child("users")
                .child(firebaseUser.getUid())
                .child("pendingFriends")
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
                        Log.e("friendSearch", databaseError.getMessage());
                    }
                });
    }

    public void addFriend(final User friend) {
        databaseReference
                .child("users")
                .child(firebaseUser.getUid())
                .child("acceptedFriends")
                .child(friend.uid)
                .setValue(friend);

        databaseReference
                .child("users")
                .child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User collageUser = dataSnapshot.getValue(User.class);
                        databaseReference
                                .child("users")
                                .child(friend.uid)
                                .child("acceptedFriends")
                                .child(firebaseUser.getUid())
                                .setValue(collageUser);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("friendSearch", databaseError.getMessage());
                    }
                });

        databaseReference
                .child("users")
                .child(firebaseUser.getUid())
                .child("pendingFriends")
                .child(friend.uid)
                .removeValue();
    }

    public void fetchFriendsList(final FriendsListener friendsListener) {
        friendsListener.onFriendsListFetchingStarted();
        databaseReference
                .child("users")
                .child(firebaseUser.getUid())
                .child("acceptedFriends")
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
                        Log.e("friendSearch", databaseError.getMessage());
                    }
                });
    }
}
