package pl.collage.login;

interface LoginView {

    void showInvalidEmailError();

    void showInvalidPasswordError();

    void hideInvalidEmailError();

    void hideInvalidPasswordError();

    void showProgressBar();

    void navigateToHome();

    void showLoginError();
}
