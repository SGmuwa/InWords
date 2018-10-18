package com.dreamproject.inwords.viewScenario.authorisation.Registration;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.dreamproject.inwords.BasePresenter;
import com.dreamproject.inwords.R;
import com.dreamproject.inwords.data.entity.UserCredentials;
import com.dreamproject.inwords.viewScenario.authorisation.SigningBaseFragment;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;

public class RegistrationFragment extends SigningBaseFragment implements RegistrationView {
    private RegistrationPresenter presenter;

    private TextInputEditText editTextConfirmPassword;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void navigateToLogin() {
        navController.navigate(R.id.action_registrationFragment_to_loginFragment_pop, null);
    }

    @Override
    public void registrationSuccess() {
        navController.navigate(R.id.action_registrationFragment_to_mainFragment_pop);

        View view = getView();
        if (view != null)
            Snackbar.make(view, "Sign up success", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void registrationError(String message) {
        View view = getView();
        if (view != null)
            Snackbar.make(view, "Sign up error: " + message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public Observable<UserCredentials> getCredentials() { //TODO show info
        return super.getCredentials().filter(userCredentials -> userCredentials.getPassword().equals(editTextConfirmPassword.getText().toString()));
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_sign_up;
    }

    @Override
    protected BasePresenter getPresenter() {
        return (BasePresenter) (presenter = new RegistrationPresenterImpl(this));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);

        presenter.onSignUpHandler(RxView.clicks(view.findViewById(R.id.buttonEnterSignUp)));
        presenter.onSignInHandler(RxView.clicks(view.findViewById(R.id.textViewSignIn)));
    }
}
