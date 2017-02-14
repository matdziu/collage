package pl.collage.util.interactors;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.UUID;

import pl.collage.base.BaseListener;
import pl.collage.friends.FriendsListener;
import pl.collage.friendsearch.FriendSearchListener;
import pl.collage.gallery.GalleryListener;
import pl.collage.util.models.Photo;
import pl.collage.util.models.User;
import timber.log.Timber;

public class FirebaseDatabaseInteractor {

    private final static String USERS = "users";
    private final static String EMAIL = "email";
    private final static String PENDING_FRIENDS = "pendingFriends";
    private final static String ACCEPTED_FRIENDS = "acceptedFriends";
    private static final String IMAGES = "images";
    private static final String IMAGE_ID = "imageId";

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
        if (firebaseUser.getEmail() != null && !firebaseUser.getEmail().equals(email)) {
            Query friendQuery = databaseReference
                    .child(USERS)
                    .orderByChild(EMAIL)
                    .equalTo(email);
            friendQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DataSnapshot friendSnapshot = dataSnapshot
                                .getChildren()
                                .iterator()
                                .next();

                        if (friendSnapshot.child(PENDING_FRIENDS).hasChild(firebaseUser.getUid())) {
                            friendSearchListener.onAlreadyInvited();
                            return;
                        }

                        if (friendSnapshot.child(ACCEPTED_FRIENDS).hasChild(firebaseUser.getUid())) {
                            friendSearchListener.onAlreadyYourFriend();
                            return;
                        }

                        friendSearchListener.onFriendFound();
                        final String friendUid = friendSnapshot.getKey();

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
        } else {
            friendSearchListener.onCantInviteYourself();
        }
    }

    public void fetchPendingList(final BaseListener<User> baseListener) {
        baseListener.onListFetchingStarted();
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
                        baseListener.onListFetched(pendingList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }

    public void addFriend(final User friend) {
        final String albumStorageId = UUID.randomUUID().toString();
        friend.albumStorageId = albumStorageId;

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
                        collageUser.albumStorageId = albumStorageId;
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

    public void fetchFriendsList(final BaseListener<User> baseListener) {
        baseListener.onListFetchingStarted();
        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(ACCEPTED_FRIENDS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<User> friendsList = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            friendsList.add(dataItem.getValue(User.class));
                        }
                        baseListener.onListFetched(friendsList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }

    public void addImage(Photo photo, User friend) {
        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(ACCEPTED_FRIENDS)
                .child(friend.uid)
                .child(IMAGES)
                .push()
                .setValue(photo);

        databaseReference
                .child(USERS)
                .child(friend.uid)
                .child(ACCEPTED_FRIENDS)
                .child(firebaseUser.getUid())
                .child(IMAGES)
                .push()
                .setValue(photo);
    }

    public void fetchPhotos(User friend, final GalleryListener galleryListener) {
        galleryListener.onListFetchingStarted();
        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(ACCEPTED_FRIENDS)
                .child(friend.uid)
                .child(IMAGES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Photo> photosList = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            photosList.add(dataItem.getValue(Photo.class));
                        }
                        galleryListener.onListFetched(photosList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }

    public void removeFriend(final User friend, final FriendsListener friendsListener) {
        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(ACCEPTED_FRIENDS)
                .child(friend.uid)
                .child(IMAGES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Photo> photosList = new ArrayList<>();
                        for (DataSnapshot dataItem : dataSnapshot.getChildren()) {
                            photosList.add(dataItem.getValue(Photo.class));
                        }
                        friendsListener.onPhotosToRemoveFetched(photosList, friend);

                        databaseReference
                                .child(USERS)
                                .child(firebaseUser.getUid())
                                .child(ACCEPTED_FRIENDS)
                                .child(friend.uid)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        friendsListener.onFriendRemovalFinished(friend);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });

        databaseReference
                .child(USERS)
                .child(friend.uid)
                .child(ACCEPTED_FRIENDS)
                .child(firebaseUser.getUid())
                .removeValue();
    }

    public void removePhoto(final String imageId, final User currentFriend,
                            final GalleryListener galleryListener) {
        databaseReference
                .child(USERS)
                .child(firebaseUser.getUid())
                .child(ACCEPTED_FRIENDS)
                .child(currentFriend.uid)
                .child(IMAGES)
                .orderByChild(IMAGE_ID)
                .equalTo(imageId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot
                                .getChildren()
                                .iterator()
                                .next()
                                .getRef()
                                .removeValue();
                        galleryListener.onPhotoRemovalFinished();
                        galleryListener.removePhotoFromStorage(imageId, currentFriend);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });

        databaseReference
                .child(USERS)
                .child(currentFriend.uid)
                .child(ACCEPTED_FRIENDS)
                .child(firebaseUser.getUid())
                .child(IMAGES)
                .orderByChild(IMAGE_ID)
                .equalTo(imageId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot
                                .getChildren()
                                .iterator()
                                .next()
                                .getRef()
                                .removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Timber.e(databaseError.getMessage());
                    }
                });
    }
}
