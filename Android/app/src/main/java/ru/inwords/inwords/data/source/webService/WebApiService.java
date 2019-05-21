package ru.inwords.inwords.data.source.webService;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.inwords.inwords.BuildConfig;
import ru.inwords.inwords.data.dto.EntityIdentificator;
import ru.inwords.inwords.data.dto.User;
import ru.inwords.inwords.data.dto.UserCredentials;
import ru.inwords.inwords.data.dto.WordTranslation;
import ru.inwords.inwords.data.dto.game.Game;
import ru.inwords.inwords.data.dto.game.GameInfo;
import ru.inwords.inwords.data.dto.game.GameLevel;
import ru.inwords.inwords.data.dto.game.LevelScore;
import ru.inwords.inwords.data.dto.game.LevelScoreRequest;
import ru.inwords.inwords.data.dto.google.TtsSynthesizeRequest;
import ru.inwords.inwords.data.dto.google.TtsSynthesizeResponse;
import ru.inwords.inwords.data.source.webService.session.TokenResponse;
import ru.inwords.inwords.data.sync.PullWordsAnswer;

public interface WebApiService {
    //GAME
    @GET("/v1.0/Game/GameInfo")
    Single<List<GameInfo>> getGameInfos(@Header("Authorization") String bearerToken);

    @GET("v1.0/Game/{gameId}")
    Single<Game> getGame(@Header("Authorization") String bearerToken, @Path("gameId") int gameId);

    @GET("v1.0/Game/level/{levelId}")
    Single<GameLevel> getLevel(@Header("Authorization") String bearerToken, @Path("levelId") int levelId);

    @POST("v1.0/Game/score")
    Single<LevelScore> getGameScore(@Header("Authorization") String bearerToken, @Body LevelScoreRequest levelScoreRequest);

    @POST("v1.0/Game/UploadScore")
    Completable uploadScore(@Header("Authorization") String bearerToken, @Body List<LevelScoreRequest> levelScoreRequests);

    //Words
    @POST("/v1.0/words/DeletePair")
    Single<Integer> deletePairs(@Header("Authorization") String bearerToken, @Body List<Integer> serverIds);

    @POST("/v1.0/words/addpair")
    Single<List<EntityIdentificator>> addPairs(@Header("Authorization") String bearerToken, @Body List<WordTranslation> wordTranslations);

    @POST("/v1.0/sync/pullwordpairs")
    Single<PullWordsAnswer> pullWordsPairs(@Header("Authorization") String bearerToken, @Body List<Integer> serverIds);

    @GET("/v1.0/values")
    Single<List<String>> getValues();

    //USERS
    @POST("/v1.0/auth/registration")
    @Headers({"Content-Type: application/json", "x-api-version: 2.0"})
    Single<TokenResponse> registerUser(@Body UserCredentials userCredentials);

    @POST("v1.0/auth/token")
    //TODO think about it
    @Headers({"x-api-version: 2.0"})
    Single<TokenResponse> getToken(@Body UserCredentials credentials);

    @GET("/v1.0/users")
    Single<User> getAuthorisedUser(@Header("Authorization") String bearerToken);

    @GET("/v1.0/users/{id}")
    Single<User> getUserById(@Header("Authorization") String bearerToken, @Path("id") int id);

    @GET("/v1.0/values/getlogin")
    Single<String> getLogin(@Header("Authorization") String bearerToken);

    //GOOGLE
    @POST(BuildConfig.TTS_GOOGLE_API_URL + "/v1beta1/text:synthesize")
    @Headers("content-type: application/json; charset=UTF-8")
    Single<TtsSynthesizeResponse> ttsSynthesize(@Header("X-Goog-Api-Key") String googleApiKey, @Body TtsSynthesizeRequest request);
}