package com.collage.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.collage.base.BaseFragment;
import com.collage.base.HomeActivity;
import com.collage.R;
import com.collage.interactors.FirebaseAuthInteractor;
import com.collage.interactors.FirebaseDatabaseInteractor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends BaseFragment implements SignUpView, SignUpResultListener {

    @BindView(R.id.edit_text_full_name)
    TextInputEditText editTextFullName;

    @BindView(R.id.edit_text_email)
    TextInputEditText editTextEmail;

    @BindView(R.id.edit_text_password)
    TextInputEditText editTextPassword;

    @BindView(R.id.text_input_layout_full_name)
    TextInputLayout textInputLayoutFullName;

    @BindView(R.id.text_input_layout_email)
    TextInputLayout textInputLayoutEmail;

    @BindView(R.id.text_input_layout_password)
    TextInputLayout textInputLayoutPassword;

    @BindView(R.id.sign_up_content_view)
    RelativeLayout signUpContectView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private SignUpPresenter signUpPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpPresenter = new SignUpPresenter(this, new FirebaseAuthInteractor(this),
                new FirebaseDatabaseInteractor());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.button_create_account)
    public void createAccount() {
        signUpPresenter.validateSignUpUserData(editTextFullName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString());
    }

    @Override
    public void showEmptyFullNameError() {
        textInputLayoutFullName.setError(getString(R.string.full_name_error));
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
    public void hideEmptyFullNameError() {
        textInputLayoutFullName.setError(null);
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
    public void onSignUpStart() {
        closeSoftKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        signUpContectView.setVisibility(View.GONE);

    }

    @Override
    public void onSignUpSuccess() {
        progressBar.setVisibility(View.GONE);
        signUpPresenter.createUserDatabaseEntry(editTextFullName.getText().toString(),
                editTextEmail.getText().toString());
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }

    @Override
    public void onSignUpFailure() {
        progressBar.setVisibility(View.GONE);
        signUpContectView.setVisibility(View.VISIBLE);
        Toast.makeText(getContext(), R.string.sign_up_failure, Toast.LENGTH_SHORT).show();
    }

}
