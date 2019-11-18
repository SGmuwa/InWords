﻿using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using InWords.Data;
using InWords.Data.Creations;
using InWords.Data.Creations.GameBox;
using InWords.Data.DTO.GameBox;
using MediatR;
using Microsoft.EntityFrameworkCore;

namespace InWords.WebApi.Services.UserGameService.GetUsersGameHistory
{
    public class GetUserGameStoryHandler : IRequestHandler<GetUserGameStoryQuery, List<LevelInfo>>
    {
        private readonly InWordsDataContext context;

        public GetUserGameStoryHandler(InWordsDataContext context)
        {
            this.context = context;
        }

        public Task<List<LevelInfo>> Handle(GetUserGameStoryQuery request, CancellationToken cancellationToken)
        {
            IQueryable<Creation> userHistoryGames = SelectUserCustomGameHistory(request);

            IQueryable<GameLevel> userHistoryLevels = SelectLevelsInGame(userHistoryGames);
            
            IQueryable<LevelInfo> levelInfos = SelectLevelInfos(userHistoryLevels);

            return levelInfos.ToListAsync(cancellationToken: cancellationToken);
        }

        private IQueryable<LevelInfo> SelectLevelInfos(IQueryable<GameLevel> userHistoryLevels)
        {
            return from level in userHistoryLevels
                join stars in context.UserGameLevels on level.GameLevelId equals stars.GameLevelId
                select new LevelInfo()
                {
                    LevelId = stars.UserGameLevelId,
                    Level = level.Level,
                    IsAvailable = true,
                    PlayerStars = stars.UserStars
                };
        }

        private IQueryable<GameLevel> SelectLevelsInGame(IQueryable<Creation> allUsersGames)
        {
            return context.GameLevels.Where(g=>allUsersGames.Any(a=>a.CreationId ==  g.GameBoxId));
        }

        private IQueryable<Creation> SelectUserCustomGameHistory(GetUserGameStoryQuery request)
        {
            return context.Creations.Where(c => c.CreationId.Equals(request.UserId));
        }
    }
}
