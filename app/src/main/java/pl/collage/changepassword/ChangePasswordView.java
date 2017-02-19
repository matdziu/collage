package pl.collage.changepassword;

interface ChangePasswordView {

    void showProgressBar();

    void hideProgressBar();

    void showPasswordTooShortError();

    void showWrongRetypeError();

    void showEmptyOldPasswordError();

    void showChangePasswordError();

    void showChangePasswordSuccess();

    void finishWithSuccess();

    void hideEmptyOldPasswordError();

    void hideNewPasswordErrors();
}
