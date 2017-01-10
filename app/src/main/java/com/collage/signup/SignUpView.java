package com.collage.signup;


interface SignUpView {

    void showEmptyFullNameError();

    void showInvalidEmailError();

    void showInvalidPasswordError();

    void createAccount(String email, String password);

    void hideEmptyFullNameError();

    void hideInvalidEmailError();

    void hideInvalidPasswordError();
}
