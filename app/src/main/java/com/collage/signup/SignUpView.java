package com.collage.signup;


interface SignUpView {

    void showEmptyFullNameError();

    void showInvalidEmailError();

    void showInvalidPasswordError();

    void showAccountCreationSuccessful();

    void hideEmptyFullNameError();

    void hideInvalidEmailError();

    void hideInvalidPasswordError();
}
