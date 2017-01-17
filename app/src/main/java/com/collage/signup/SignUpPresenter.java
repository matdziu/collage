package com.collage.signup;


import android.util.Patterns;

import com.collage.interactors.FirebaseAuthInteractor;

class SignUpPresenter {

    private SignUpView signUpView;
    private FirebaseAuthInteractor firebaseAuthInteractor;


    SignUpPresenter(SignUpView signUpView, FirebaseAuthInteractor firebaseAuthInteractor) {
        this.signUpView = signUpView;
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void validateSignUpUserData(String fullName,
                                String email,
                                String password) {

        boolean isFullNameValid;
        boolean isEmailValid;
        boolean isPasswordValid;

        if (fullName.replaceAll("\\s+", "").isEmpty()) {
            signUpView.showEmptyFullNameError();
            isFullNameValid = false;
        } else {
            signUpView.hideEmptyFullNameError();
            isFullNameValid = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpView.showInvalidEmailError();
            isEmailValid = false;
        } else {
            signUpView.hideInvalidEmailError();
            isEmailValid = true;
        }

        if (password.length() < 6 ||
                password.contains(" ")) {
            signUpView.showInvalidPasswordError();
            isPasswordValid = false;
        } else {
            signUpView.hideInvalidPasswordError();
            isPasswordValid = true;
        }

        if (isEmailValid && isFullNameValid && isPasswordValid) {
            firebaseAuthInteractor.createAccount(email, password);
        }
    }
}
