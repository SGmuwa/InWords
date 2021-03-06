package ru.inwords.inwords.game.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.inwords.inwords.core.resource.Resource
import ru.inwords.inwords.game.data.bean.GameLevel
import ru.inwords.inwords.game.data.bean.LevelScore
import ru.inwords.inwords.game.data.bean.LevelScoreRequest
import ru.inwords.inwords.game.domain.model.Game
import ru.inwords.inwords.game.domain.model.GameInfo
import ru.inwords.inwords.game.domain.model.GamesInfo
import ru.inwords.inwords.game.domain.model.LevelResultModel

interface CustomGameGatewayController {
    fun storeGameInfo(gameInfo: GameInfo): Completable
    fun storeGame(game: Game): Completable
    fun getGame(gameId: Int, forceUpdate: Boolean): Observable<Resource<Game>>
    fun storeLevel(gameLevel: GameLevel): Completable
}

interface GameGatewayController {
    fun getGamesInfo(forceUpdate: Boolean = false): Observable<Resource<GamesInfo>>
    fun getGame(gameId: Int, forceUpdate: Boolean = false): Observable<Resource<Game>>
    fun getLevel(levelId: Int, forceUpdate: Boolean = false): Observable<Resource<GameLevel>>
    fun getScore(game: Game, levelResultModel: LevelResultModel): Single<Resource<LevelScore>>
    fun uploadScoresToServer(): Single<List<LevelScoreRequest>>
    fun addWordsToUserDictionary(gameId: Int): Completable
    fun clearCache()
}