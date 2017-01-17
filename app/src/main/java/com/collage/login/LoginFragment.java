package com.collage.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.collage.BaseFragment;
import com.collage.HomeActivity;
import com.collage.R;
import com.collage.interactors.FirebaseAuthInteractor;
import com.collage.signup.SignUpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements LoginView, LoginResultListener {

    @BindView(R.id.edit_text_email)
    TextInputEditText editTextEmail;

    @BindView(R.id.edit_text_password)
    TextInputEditText editTextPassword;

    @BindView(R.id.text_input_layout_email)
    TextInputLayout textInputLayoutEmail;

    @BindView(R.id.text_input_layout_password)
    TextInputLayout textInputLayoutPassword;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.login_content_view)
    RelativeLayout loginContentView;

    private LoginPresenter loginPresenter;
    private FirebaseAuthInteractor firebaseAuthInteractor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuthInteractor = new FirebaseAuthInteractor(this);
        loginPresenter = new LoginPresenter(this, firebaseAuthInteractor);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuthInteractor.addAuthStateListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuthInteractor.removeAuthStateListener();
    }

    @OnClick(R.id.button_sign_up)
    public void signUp() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.linear_layout_activity_login, new SignUpFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @OnClick(R.id.button_login)
    public void login() {
        loginPresenter.validateLoginUserData(editTextEmail.getText().toString(),
                editTextPassword.getText().toString());
    }

    @Override
    public void showInvalidEmailError() {
        textInputLayoutEmail.setError(getString(R.string.email_error));
    }

    @Override
    public void showInvalidPasswordError() {
        textInputLayoutPassword.setError(getString(R.string.password_error));
    }

    @Override
    public void hideInvalidEmailError() {
        textInputLayoutEmail.setError(null);
    }

    @Override
    public void hideInvalidPasswordError() {
        textInputLayoutPassword.setError(null);
    }

    @Override
    public void onLoginStart() {
        closeSoftKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        loginContentView.setVisibility(View.GONE);
    }

    @Override
    public void onLoginSuccess() {
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }

    @Override
    public void onLoginFailure() {
        progressBar.setVisibility(View.GONE);
        loginContentView.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), R.string.login_failure, Toast.LENGTH_SHORT).show();
    }
}
