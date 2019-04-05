package com.dreamproject.inwords.domain.model

import com.dreamproject.inwords.data.dto.game.GameInfo
import java.io.Serializable

data class GamesInfoModel(val shouldShowIntro: Boolean,
                          val gameInfos: List<GameInfo>) : Serializable
