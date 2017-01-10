package com.collage.login;


interface LoginView {

    void showInvalidEmailError();

    void showInvalidPasswordError();

    void hideInvalidEmailError();

    void hideInvalidPasswordError();
}
