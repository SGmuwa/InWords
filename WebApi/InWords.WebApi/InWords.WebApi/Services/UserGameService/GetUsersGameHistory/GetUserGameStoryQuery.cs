﻿using InWords.Data.DTO.GameBox;
using MediatR;

namespace InWords.WebApi.Services.UserGameService.GetUsersGameHistory
{
    public class GetUserGameStoryQuery : IRequest<LevelInfo>
    {
        public int UserId { get; set; }
        public int StoryCount { get; set; }
        public int Page { get; set; }
    }
}
