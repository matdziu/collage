package com.collage.home;


import com.collage.interactors.FirebaseAuthInteractor;

class HomePresenter {

    private FirebaseAuthInteractor firebaseAuthInteractor;

    HomePresenter(FirebaseAuthInteractor firebaseAuthInteractor) {
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void signOut() {
        firebaseAuthInteractor.signOut();
    }
}
