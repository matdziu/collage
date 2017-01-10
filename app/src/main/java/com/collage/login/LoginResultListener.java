package com.collage.login;

public interface LoginResultListener {

    void onLoginStart();

    void onLoginSuccess();

    void onLoginFailure();
}
