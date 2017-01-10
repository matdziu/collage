package com.collage.signup;

public interface SignUpResultListener {

    void onSignUpStart();

    void onSignUpSuccess();

    void onSignUpFailure();
}
