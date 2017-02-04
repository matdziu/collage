package com.collage.base;

import com.collage.util.interactors.FirebaseAuthInteractor;
import com.collage.util.models.User;

import java.util.ArrayList;
import java.util.List;

public class BasePresenter {

    private FirebaseAuthInteractor firebaseAuthInteractor;
    protected List<User> usersList;
    protected List<User> filteredList;

    protected BasePresenter() {
        // default constructor
    }

    BasePresenter(FirebaseAuthInteractor firebaseAuthInteractor) {
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void signOut() {
        firebaseAuthInteractor.signOut();
    }

    void filterUsers(BaseUsersView baseUsersView, String query) {
        List<User> filteredList = new ArrayList<>();
        if (usersList != null) {
            for (User user : usersList) {
                if (user.fullName.toLowerCase()
                        .contains(query.toLowerCase())) {
                    filteredList.add(user);
                }
            }
            this.filteredList = filteredList;
            baseUsersView.updateRecyclerView(filteredList);
        }
    }
}
