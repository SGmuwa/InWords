package ru.inwords.inwords.data.repository.game

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Single
import ru.inwords.inwords.core.util.SchedulersFacade
import ru.inwords.inwords.data.dto.game.*
import ru.inwords.inwords.data.source.database.game.GameDao
import ru.inwords.inwords.data.source.database.game.GameInfoDao
import ru.inwords.inwords.data.source.database.game.GameLevelDao
import ru.inwords.inwords.data.source.database.game.LevelScoreRequestDao
import ru.inwords.inwords.domain.model.Resource
import javax.inject.Inject

@SuppressLint("UseSparseArrays")
class GameGatewayControllerImpl @Inject constructor(
        private val gameRemoteRepository: GameRemoteRepository,
        gameInfoDao: GameInfoDao,
        gameDao: GameDao,
        gameLevelDao: GameLevelDao,
        private val levelScoreRequestDao: LevelScoreRequestDao) : GameGatewayController {

    private val gameInfoDatabaseRepository by lazy { GameDatabaseRepository<GameInfo>(gameInfoDao) }
    private val gameDatabaseRepository by lazy { GameDatabaseRepository<Game>(gameDao) }
    private val gameLevelDatabaseRepository by lazy { GameDatabaseRepository<GameLevel>(gameLevelDao) }

    private val gamesInfoCachingProvider by lazy { createGamesInfoCachingProvider() }
    private val gameCachingProviderLocator by lazy { ResourceCachingProvider.Locator<Game>() }
    private val gameLevelCachingProviderLocator by lazy { ResourceCachingProvider.Locator<GameLevel>() }

    override fun getGamesInfo(forceUpdate: Boolean): Observable<Resource<List<GameInfo>>> {
        if (forceUpdate) {
            gamesInfoCachingProvider.askForContentUpdate()
        }

        return gamesInfoCachingProvider.observe()
    }

    override fun getGame(gameId: Int, forceUpdate: Boolean): Observable<Resource<Game>> {
        val cachingProvider = gameCachingProviderLocator.get(gameId) { createGameCachingProvider(gameId) }

        if (forceUpdate) {
            cachingProvider.askForContentUpdate()
        }

        return cachingProvider.observe()
    }

    override fun getLevel(levelId: Int, forceUpdate: Boolean): Observable<Resource<GameLevel>> {
        val cachingProvider = gameLevelCachingProviderLocator.get(levelId) { createGameLevelCachingProvider(levelId) }

        if (forceUpdate) {
            cachingProvider.askForContentUpdate()
        }

        return cachingProvider.observe()
    }

    override fun getScore(game: Game, levelScoreRequest: LevelScoreRequest): Single<Resource<LevelScore>> {
        return gameRemoteRepository.getScore(levelScoreRequest).wrapResource()
                .flatMap { res ->
                    if (res.error()) {
                        levelScoreRequestDao.insert(levelScoreRequest)
                                .doOnError { Log.d(TAG, it.message) }
                                .map { res }
                                .onErrorReturn { res }
                    } else {
                        updateLocalScore(game, res.data!!)
                        Single.just(res)
                    }
                }
                .subscribeOn(SchedulersFacade.io())
    }

    private fun updateLocalScore(game: Game, levelScore: LevelScore) {
        val list = game.gameLevelInfos.map {
            if (it.levelId == levelScore.levelId) {
                it.copy(playerStars = levelScore.score)
            } else {
                it
            }
        }

        val updatedGame = game.copy(gameLevelInfos = list)

        val cachingProvider = gameCachingProviderLocator.get(updatedGame.gameId) { createGameCachingProvider(updatedGame.gameId) }

        cachingProvider.postOnLoopback(updatedGame)
    }

    private fun createGamesInfoCachingProvider(): ResourceCachingProvider<List<GameInfo>> {
        return ResourceCachingProvider(
                { data -> gameInfoDatabaseRepository.insertAll(data).map { data } },
                { gameInfoDatabaseRepository.getAll() },
                { gameRemoteRepository.getGameInfos() }
        )
    }

    private fun createGameCachingProvider(gameId: Int): ResourceCachingProvider<Game> {
        return ResourceCachingProvider(
                { data -> gameDatabaseRepository.insertAll(listOf(data)).map { data } },
                { gameDatabaseRepository.getById(gameId) },
                { gameRemoteRepository.getGame(gameId) }
        )
    }

    private fun createGameLevelCachingProvider(levelId: Int): ResourceCachingProvider<GameLevel> {
        return ResourceCachingProvider(
                { data -> gameLevelDatabaseRepository.insertAll(listOf(data)).map { data } },
                { gameLevelDatabaseRepository.getById(levelId) },
                { gameRemoteRepository.getLevel(levelId) }
        )
    }

    companion object {
        const val TAG: String = "GameGatewayController"
    }
}

