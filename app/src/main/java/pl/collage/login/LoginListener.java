package pl.collage.login;

public interface LoginListener {

    void onLoginStart();

    void onLoginSuccess();

    void onLoginFailure();
}
