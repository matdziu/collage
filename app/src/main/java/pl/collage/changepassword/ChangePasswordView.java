package pl.collage.changepassword;

interface ChangePasswordView {

    void showProgressBar();

    void hideProgressBar();

    void showPasswordTooShortError();

    void showWrongRetypeError();

    void hideErrors();
}
