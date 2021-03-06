﻿using InWords.Data.DTO.GameBox;
using InWords.Data.DTO.GameBox.LevelMetric;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace InWords.WebApi.Services.Abstractions
{
    public interface IGameScoreService
    {
        /// <summary>
        ///     This is to read user score on game level
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="game"></param>
        /// <returns></returns>
        Task<GameObject> GetGameStarsAsync(int userId, GameObject game);

        /// <summary>
        ///     This is to update user score on game level
        /// </summary>
        /// <param name="levelResult"></param>
        /// <returns></returns>
        LevelMetricQueryResult GetLevelScore(LevelResult levelResult);

        /// <summary>
        ///     This is to set level score to user level storage
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="levelMetricQueryResult"></param>
        /// <exception cref="ArgumentNullException">Null game box is not find</exception>
        /// <returns></returns>
        Task PostScoreAsync(int userId, LevelMetricQueryResult levelMetricQueryResult);

        /// <summary>
        ///     This is to push all score from cache storage
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="levelScores"></param>
        /// <returns></returns>
        Task UploadScoreAsync(int userId, IEnumerable<LevelMetricQueryResult> levelScores);
    }
}