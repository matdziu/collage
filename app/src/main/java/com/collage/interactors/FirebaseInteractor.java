package com.collage.interactors;


import android.support.annotation.NonNull;

import com.collage.login.LoginResultListener;
import com.collage.signup.SignUpResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseInteractor {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private LoginResultListener loginResultListener;
    private SignUpResultListener signUpResultListener;

    public FirebaseInteractor(LoginResultListener loginResultListener) {
        this.loginResultListener = loginResultListener;
    }

    public FirebaseInteractor(SignUpResultListener signUpResultListener) {
        this.signUpResultListener = signUpResultListener;
    }

    public void createAccount(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signUpResultListener.onSignUpFailure();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        signUpResultListener.onSignUpSuccess();
                    }
                });
    }

    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginResultListener.onLoginFailure();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loginResultListener.onLoginSuccess();
                    }
                });
    }

    public void signOut() {
        firebaseAuth.signOut();
    }
}
