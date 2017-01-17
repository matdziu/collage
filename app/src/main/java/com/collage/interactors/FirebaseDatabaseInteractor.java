package com.collage.interactors;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseInteractor {

    private DatabaseReference databaseReference =
            FirebaseDatabase.getInstance().getReference();

    public void createUserDatabaseEntry(String fullName, String email) {
        FirebaseUser user = FirebaseAuth.getInstance()
                .getCurrentUser();
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
}
