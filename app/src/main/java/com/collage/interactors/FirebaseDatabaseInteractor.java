package com.collage.interactors;


import android.util.Log;

import com.collage.friendsearch.FriendSearchResultListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseInteractor {

    private DatabaseReference databaseReference =
            FirebaseDatabase.getInstance().getReference();
    private FirebaseUser user = FirebaseAuth.getInstance()
            .getCurrentUser();
    private FriendSearchResultListener friendsResultListener;

    public FirebaseDatabaseInteractor() {
        // default constructor
    }

    public FirebaseDatabaseInteractor(FriendSearchResultListener friendsResultListener) {
        this.friendsResultListener = friendsResultListener;
    }

    public void createUserDatabaseEntry(String fullName, String email) {
        if (user != null) {
            databaseReference
                    .child("users")
                    .child(user.getUid())
                    .child("fullName")
                    .setValue(fullName);
            databaseReference
                    .child("users")
                    .child(user.getUid())
                    .child("email")
                    .setValue(email);
        }
    }

    public void searchForFriend(String email) {
        Query friendQuery = databaseReference
                .child("users")
                .orderByChild("email")
                .equalTo(email);
        friendQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    friendsResultListener.onFriendFound();

                    String friendUid = dataSnapshot
                            .getChildren()
                            .iterator()
                            .next()
                            .getKey();

                    databaseReference
                            .child("users")
                            .child(friendUid)
                            .child("friends")
                            .child(user.getUid())
                            .setValue(false);

                } else {
                    friendsResultListener.onFriendNotFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("friendSearch", databaseError.getMessage());
            }
        });
    }
}
