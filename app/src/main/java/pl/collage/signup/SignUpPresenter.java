package pl.collage.signup;

import android.util.Patterns;

import pl.collage.util.interactors.FirebaseAuthInteractor;
import pl.collage.util.interactors.FirebaseDatabaseInteractor;

class SignUpPresenter implements SignUpListener {

    private SignUpView signUpView;
    private FirebaseAuthInteractor firebaseAuthInteractor;
    private FirebaseDatabaseInteractor firebaseDatabaseInteractor;


    SignUpPresenter(SignUpView signUpView, FirebaseAuthInteractor firebaseAuthInteractor,
                    FirebaseDatabaseInteractor firebaseDatabaseInteractor) {
        this.signUpView = signUpView;
        this.firebaseAuthInteractor = firebaseAuthInteractor;
        this.firebaseDatabaseInteractor = firebaseDatabaseInteractor;
    }

    void validateSignUpUserData(String fullName,
                                String email,
                                String password) {

        boolean isFullNameValid;
        boolean isEmailValid;
        boolean isPasswordValid;

        if (fullName.replaceAll("\\s+", "").isEmpty()) {
            signUpView.showEmptyFullNameError();
            isFullNameValid = false;
        } else {
            signUpView.hideEmptyFullNameError();
            isFullNameValid = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpView.showInvalidEmailError();
            isEmailValid = false;
        } else {
            signUpView.hideInvalidEmailError();
            isEmailValid = true;
        }

        if (password.length() < 6 ||
                password.contains(" ")) {
            signUpView.showInvalidPasswordError();
            isPasswordValid = false;
        } else {
            signUpView.hideInvalidPasswordError();
            isPasswordValid = true;
        }

        if (isEmailValid && isFullNameValid && isPasswordValid) {
            firebaseAuthInteractor.createAccount(email, password, this);
        }
    }

    @Override
    public void onSignUpStart() {
        signUpView.showProgressBar();
    }

    @Override
    public void onSignUpSuccess() {
        firebaseDatabaseInteractor.createUserDatabaseEntry(signUpView.getFullName(),
                signUpView.getEmail());
        signUpView.navigateToHome();
    }

    @Override
    public void onSignUpFailure() {
        signUpView.showSignUpError();
    }
}
