package ru.inwords.inwords.domain.converter

import ru.inwords.inwords.R
import ru.inwords.inwords.core.BaseOneWayConverter
import ru.inwords.inwords.core.ResourceManager
import ru.inwords.inwords.data.dto.game.GameInfo
import ru.inwords.inwords.data.repository.game.custom_game.CUSTOM_GAME_ID
import ru.inwords.inwords.domain.model.GameInfoModel

class GameInfoDomainConverter(private val resourceManager: ResourceManager) : BaseOneWayConverter<GameInfo, GameInfoModel>() {
    override fun convert(source: GameInfo): GameInfoModel { //TODO check userId on customGames
        val title = source.title ?: resourceManager.getString(R.string.custom_game)

        return GameInfoModel(
            gameId = source.gameId,
            creatorId = source.creatorId,
            description = source.description.orEmpty(), //TODO description of custom game
            title = title,
            available = source.available,
            isCustom = source.gameId == CUSTOM_GAME_ID
        )
    }
}