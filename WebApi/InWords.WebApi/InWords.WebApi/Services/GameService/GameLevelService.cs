﻿using InWords.Data.Creations;
using InWords.Data.Creations.GameBox;
using InWords.Data.DTO.GameBox;
using InWords.Data.Repositories;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace InWords.WebApi.Services.GameService
{
    [Obsolete]
    public class GameLevelService
    {
        private readonly GameLevelRepository gameLevelRepository;
        private readonly GameLevelWordService gameLevelWordService;

        public GameLevelService(GameLevelRepository gameLevelRepository,
            GameLevelWordService gameLevelWordService)
        {
            this.gameLevelRepository = gameLevelRepository;
            this.gameLevelWordService = gameLevelWordService;
        }

        public async Task<GameLevel> AddLevelAsync(Game gameBox, LevelPack levelPack)
        {
            var gameLevel = new GameLevel
            {
                GameId = gameBox.GameId,
                Level = levelPack.Level
            };
            gameLevel = await gameLevelRepository.CreateAsync(gameLevel).ConfigureAwait(false);
            await gameLevelWordService.AddWordsToLevelAsync(levelPack.WordTranslations, gameLevel.GameLevelId)
                .ConfigureAwait(false);
            return gameLevel;
        }

        public IEnumerable<LevelInfo> GetLevels(Game gameBox)
        {
            // find all game levels 
            IEnumerable<GameLevel> gameLevels = gameLevelRepository.GetWhere(l => l.GameId == gameBox.GameId);
            // convert to level info
            return gameLevels.Select(GetLevelInfo);
        }

        public LevelInfo GetLevelInfo(GameLevel level)
        {
            var levelInfo = new LevelInfo
            {
                IsAvailable = true,
                LevelId = level.GameLevelId,
                Level = level.Level,
                PlayerStars = 0
            };
            return levelInfo;
        }
    }
}