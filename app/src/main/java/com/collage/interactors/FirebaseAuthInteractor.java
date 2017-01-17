package com.collage.interactors;


import android.support.annotation.NonNull;

import com.collage.login.LoginResultListener;
import com.collage.signup.SignUpResultListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthInteractor {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                loginResultListener.onLoginSuccess();
            }
        }
    };

    private LoginResultListener loginResultListener;
    private SignUpResultListener signUpResultListener;

    public FirebaseAuthInteractor(LoginResultListener loginResultListener) {
        this.loginResultListener = loginResultListener;
    }

    public FirebaseAuthInteractor(SignUpResultListener signUpResultListener) {
        this.signUpResultListener = signUpResultListener;
    }

    public void createAccount(String email, String password) {
        signUpResultListener.onSignUpStart();
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
        loginResultListener.onLoginStart();
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

    public void addAuthStateListener() {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void removeAuthStateListener() {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
