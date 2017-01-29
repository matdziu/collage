package com.collage.util.interactors;

import android.support.annotation.NonNull;

import com.collage.login.LoginListener;
import com.collage.signup.SignUpListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthInteractor {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;

    public void createAccount(String email, String password,
                              final SignUpListener signUpListener) {
        signUpListener.onSignUpStart();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signUpListener.onSignUpFailure();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        signUpListener.onSignUpSuccess();
                    }
                });
    }

    public void signIn(String email, String password, final LoginListener loginListener) {
        loginListener.onLoginStart();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginListener.onLoginFailure();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        loginListener.onLoginSuccess();
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

    public void initAuthStateListener(final LoginListener loginListener) {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    loginListener.onLoginSuccess();
                }
            }
        };
    }
}
