package com.collage.signup;


interface SignUpView {

    void showEmptyFullNameError();

    void showInvalidEmailError();

    void showInvalidPasswordError();

    void hideEmptyFullNameError();

    void hideInvalidEmailError();

    void hideInvalidPasswordError();

    void showProgressBar();

    void navigateToHome();

    void showSignUpError();

    String getFullName();

    String getEmail();
}
