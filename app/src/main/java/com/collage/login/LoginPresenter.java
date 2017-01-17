package com.collage.login;

import android.util.Patterns;

import com.collage.interactors.FirebaseAuthInteractor;


class LoginPresenter {

    private LoginView loginView;
    private FirebaseAuthInteractor firebaseAuthInteractor;

    LoginPresenter(LoginView loginView, FirebaseAuthInteractor firebaseAuthInteractor) {
        this.loginView = loginView;
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void validateLoginUserData(String email,
                               String password) {

        boolean isEmailValid;
        boolean isPasswordValid;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginView.showInvalidEmailError();
            isEmailValid = false;
        } else {
            loginView.hideInvalidEmailError();
            isEmailValid = true;
        }

        if (password.length() < 6 ||
                password.contains(" ")) {
            loginView.showInvalidPasswordError();
            isPasswordValid = false;
        } else {
            loginView.hideInvalidPasswordError();
            isPasswordValid = true;
        }

        if (isEmailValid && isPasswordValid) {
            firebaseAuthInteractor.signIn(email, password);
        }
    }

    void addAuthStateListener() {
        firebaseAuthInteractor.addAuthStateListener();
    }

    void removeAuthStateListener() {
        firebaseAuthInteractor.removeAuthStateListener();
    }

}
