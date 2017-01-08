package com.collage.login;


interface LoginView {

    void showInvalidEmailError();

    void showInvalidPasswordError();

    void showLoginSuccessful();

    void hideInvalidEmailError();

    void hideInvalidPasswordError();
}
