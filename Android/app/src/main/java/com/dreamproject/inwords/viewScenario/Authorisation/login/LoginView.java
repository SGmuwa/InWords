package com.dreamproject.inwords.viewScenario.Authorisation.login;

import com.dreamproject.inwords.data.entity.UserCredentials;

import io.reactivex.Observable;

public interface LoginView {
    Observable<UserCredentials> getCredentials();
    void loginSuccess();
    void loginError();
    void navigateToRegistration();
}