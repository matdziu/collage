package pl.collage.changepassword;

import pl.collage.util.interactors.FirebaseAuthInteractor;

class ChangePasswordPresenter implements ChangePasswordListener {

    private ChangePasswordView changePasswordView;
    private FirebaseAuthInteractor firebaseAuthInteractor;

    ChangePasswordPresenter(ChangePasswordView changePasswordView, FirebaseAuthInteractor firebaseAuthInteractor) {
        this.changePasswordView = changePasswordView;
        this.firebaseAuthInteractor = firebaseAuthInteractor;
    }

    void validatePasswordChange(String oldPassword, String newPassword, String retypePassword) {
        boolean isOldPasswordCorrect;
        boolean isNewPasswordCorrect;
        boolean isRetypePasswordCorrect;

        if (oldPassword.replaceAll("\\s+", "").isEmpty()) {
            isOldPasswordCorrect = false;
            changePasswordView.showEmptyOldPasswordError();
        } else {
            isOldPasswordCorrect = true;
            changePasswordView.hideEmptyOldPasswordError();
        }

        if (newPassword.replaceAll("\\s+", "").isEmpty() || newPassword.contains(" ") ||
                newPassword.length() < 6) {
            isNewPasswordCorrect = false;
            changePasswordView.showPasswordTooShortError();
        } else {
            isNewPasswordCorrect = true;
        }

        if (!newPassword.equals(retypePassword)) {
            isRetypePasswordCorrect = false;
            changePasswordView.showWrongRetypeError();
        } else {
            isRetypePasswordCorrect = true;
        }

        if (isNewPasswordCorrect && isRetypePasswordCorrect) {
            changePasswordView.hideNewPasswordErrors();
        }

        if (isOldPasswordCorrect && isNewPasswordCorrect && isRetypePasswordCorrect) {
            changePasswordView.showProgressBar();
            firebaseAuthInteractor.changePassword(oldPassword, newPassword, this);
        }
    }

    @Override
    public void showError() {
        changePasswordView.showChangePasswordError();
        changePasswordView.hideProgressBar();
    }

    @Override
    public void showSuccess() {
        changePasswordView.showChangePasswordSuccess();
        changePasswordView.finishWithSuccess();
    }
}
