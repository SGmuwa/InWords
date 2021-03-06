package ru.inwords.inwords.data.source.remote

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ru.inwords.inwords.core.rxjava.SchedulersFacade
import ru.inwords.inwords.data.source.grpc.ProfileGrpcService
import ru.inwords.inwords.data.source.remote.session.AuthInfo
import ru.inwords.inwords.data.source.remote.session.SessionHelper
import ru.inwords.inwords.data.source.remote.session.TokenResponse
import ru.inwords.inwords.data.source.remote.session.requireCredentials
import ru.inwords.inwords.profile.data.bean.UserCredentials
import ru.inwords.inwords.texttospeech.data.bean.TtsSynthesizeRequest
import javax.inject.Inject

class WebRequestsManagerUnauthorisedImpl @Inject internal constructor(
    private val apiServiceUnauthorised: ApiServiceUnauthorised,
    private val sessionHelper: SessionHelper,
    private val profileGrpcService: ProfileGrpcService,
    private val authInfo: AuthInfo
) : WebRequestsManagerUnauthorised {

    private val authenticatedNotifierSubject = BehaviorSubject.create<Boolean>()

    override val authenticatedNotifier: Observable<Boolean> get() = authenticatedNotifierSubject

    override fun getToken(): Single<TokenResponse> {
        return authInfo.getCredentials().updateToken()
    }

    override fun getToken(userCredentials: UserCredentials): Single<TokenResponse> {
        return Single.fromCallable { userCredentials.requireCredentials() }
            .updateToken()
            .flatMap { tokenResponse ->
                authInfo.setCredentials(userCredentials)
                    .map { tokenResponse }
            }
    }

    private fun Single<UserCredentials>.updateToken(): Single<TokenResponse> {
        return flatMap { profileGrpcService.getToken(it) }
            .flatMap { setAuthToken(it) }
            .applyAuthSessionHelper()
            .subscribeOn(SchedulersFacade.io())
    }

    private fun setAuthToken(tokenResponse: TokenResponse): Single<TokenResponse> {
        return Single.fromCallable {
            this.authInfo.tokenResponse = tokenResponse

            tokenResponse
        }
    }

    override fun registerUser(userCredentials: UserCredentials): Single<TokenResponse> {
        return profileGrpcService.register(userCredentials)
            .applyAuthSessionHelper()
            .zipWith(authInfo.setCredentials(userCredentials),
                BiFunction<TokenResponse, UserCredentials, TokenResponse> { tokenResponse, u -> tokenResponse })
            .subscribeOn(Schedulers.io())
    }

    override fun ttsSynthesize(request: TtsSynthesizeRequest, googleServicesApiKey: String): Single<String> { //TODO
        return apiServiceUnauthorised.ttsSynthesize(googleServicesApiKey, request)
            .map { it.audioContent }
    }

    private fun Single<TokenResponse>.applyAuthSessionHelper(): Single<TokenResponse> {
        return doOnSuccess { sessionHelper.resetThreshold() }
            .onErrorResumeNext { throwable ->
                Log.e(javaClass.simpleName, throwable.message.orEmpty())

                if (sessionHelper.interceptAuthError(throwable)) {
                    setAuthToken(AuthInfo.unauthorisedToken) //TODO this has no sense in real
                        .flatMap { Single.error<TokenResponse>(throwable) }
                } else {
                    Single.error<TokenResponse>(throwable)
                }
            }
            .flatMap { setAuthToken(it) }
            .doFinally { authenticatedNotifierSubject.onNext(!authInfo.isUnauthorised) }
    }
}
