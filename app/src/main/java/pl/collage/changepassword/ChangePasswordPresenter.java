package pl.collage.changepassword;

import pl.collage.util.interactors.FirebaseAuthInteractor;

class ChangePasswordPresenter {

    private ChangePasswordView changePasswordView;
    private FirebaseAuthInteractor firebaseAuthInteractor;

    ChangePasswordPresenter(ChangePasswordView changePasswordView, FirebaseAuthInteractor firebaseAuthInteractor) {
        this.changePasswordView = changePasswordView;
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void validatePasswordChange(String oldPassword, String newPassword, String retypePassword) {

        if (newPassword.replaceAll("\\s+", "").isEmpty() || newPassword.contains(" ") ||
                newPassword.length() < 6) {
            changePasswordView.showPasswordTooShortError();
            return;
        }

        if (!newPassword.equals(retypePassword)) {
            changePasswordView.showWrongRetypeError();
            return;
        }

        changePasswordView.hideErrors();
    }
}
