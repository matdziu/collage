package com.collage.base;


import com.collage.interactors.FirebaseAuthInteractor;

class BasePresenter {

    private FirebaseAuthInteractor firebaseAuthInteractor;

    BasePresenter(FirebaseAuthInteractor firebaseAuthInteractor) {
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void signOut() {
        firebaseAuthInteractor.signOut();
    }
}
