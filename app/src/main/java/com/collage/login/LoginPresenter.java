package com.collage.login;

import android.util.Patterns;

import com.collage.interactors.FirebaseInteractor;


class LoginPresenter {

    private LoginView loginView;
    private FirebaseInteractor firebaseInteractor;

    LoginPresenter(LoginView loginView, FirebaseInteractor firebaseInteractor) {
        this.loginView = loginView;
        this.firebaseInteractor = firebaseInteractor;
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
            firebaseInteractor.signIn(email, password);
        }
    }

}
