package com.dreamproject.inwords.presentation.viewScenario.octoGame.games

import android.util.Log
import com.dreamproject.inwords.data.dto.game.GameInfo
import com.dreamproject.inwords.domain.interactor.game.GameInteractor
import com.dreamproject.inwords.domain.model.GamesInfoModel
import com.dreamproject.inwords.presentation.viewScenario.BasicViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class GamesViewModel(private val gameInteractor: GameInteractor) : BasicViewModel() {
    private val _navigateToGameLevelSubject: Subject<GameInfo> = PublishSubject.create()

    val navigateToGame: Observable<GameInfo>
        get() = _navigateToGameLevelSubject

    fun onGameSelected(gameInfo: GameInfo) {
        _navigateToGameLevelSubject.onNext(gameInfo)
    }

    fun screenInfoStream(): Observable<GamesInfoModel> = gameInteractor.getGamesInfo()

    fun onGameRemoved(gameInfo: GameInfo) {
        Log.d("onGameRemoved", gameInfo.toString())
    }
}