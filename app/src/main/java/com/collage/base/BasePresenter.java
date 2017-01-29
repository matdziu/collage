package com.collage.base;

import com.collage.util.interactors.FirebaseAuthInteractor;

class BasePresenter {

    private FirebaseAuthInteractor firebaseAuthInteractor;

    BasePresenter(FirebaseAuthInteractor firebaseAuthInteractor) {
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void signOut() {
        firebaseAuthInteractor.signOut();
    }
}
