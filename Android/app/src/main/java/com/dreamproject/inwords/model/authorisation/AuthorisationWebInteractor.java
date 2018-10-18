package com.dreamproject.inwords.model.authorisation;

import com.dreamproject.inwords.data.entity.UserCredentials;
import com.dreamproject.inwords.data.source.WebService.AuthToken;
import com.dreamproject.inwords.data.source.WebService.AuthenticationError;
import com.dreamproject.inwords.data.source.WebService.WebRequests;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;

public class AuthorisationWebInteractor implements AuthorisationInteractor {
    private WebRequests webRequests;

    public AuthorisationWebInteractor(WebRequests webRequests) {
        this.webRequests = webRequests;
    }

    @Override
    public Completable signIn(UserCredentials userCredentials) {
        return Completable.create((emitter) -> {
            webRequests.setCredentials(userCredentials);
            AuthToken authToken = webRequests.updateToken();

            checkAuthToken(emitter, authToken);
        });
    }

    @Override
    public Completable signUp(UserCredentials userCredentials) {
        return Completable.create((emitter) -> {
            AuthToken authToken = webRequests.registerUser(userCredentials)
                    .onErrorReturnItem(AuthToken.errorToken())
                    .blockingGet();

            checkAuthToken(emitter, authToken);
        });
    }

    private void checkAuthToken(CompletableEmitter emitter, AuthToken authToken) {
        if (authToken.isValid())
            emitter.onComplete();
        else
            emitter.onError(new AuthenticationError("error"));
    }
}
