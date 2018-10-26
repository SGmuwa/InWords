package com.dreamproject.inwords.model.authorisation;

import com.dreamproject.inwords.data.entity.UserCredentials;
import com.dreamproject.inwords.data.source.WebService.AuthenticationError;
import com.dreamproject.inwords.data.source.WebService.TokenResponse;
import com.dreamproject.inwords.data.source.WebService.WebRequests;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.Single;
import retrofit2.HttpException;

public class AuthorisationWebInteractor implements AuthorisationInteractor {
    private WebRequests webRequests;

    @Inject
    AuthorisationWebInteractor(WebRequests webRequests) {
        this.webRequests = webRequests;
    }

    @Override
    public Completable signIn(UserCredentials userCredentials) {
        return Completable.create((emitter) -> {
            webRequests.setCredentials(userCredentials);

            checkAuthToken(emitter, webRequests.updateToken());
        });
    }

    @Override
    public Completable signUp(UserCredentials userCredentials) {
        return Completable.create((emitter) -> {
            checkAuthToken(emitter, webRequests.registerUser(userCredentials));
        });
    }

    private void checkAuthToken(CompletableEmitter emitter, Single<TokenResponse> authTokenSingle) {
        try {
            TokenResponse tokenResponse = authTokenSingle.blockingGet();

            if (tokenResponse.isValid())
                emitter.onComplete();
        } catch (HttpException e) {
            try {
                emitter.onError(new AuthenticationError(e.response().errorBody().string()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
