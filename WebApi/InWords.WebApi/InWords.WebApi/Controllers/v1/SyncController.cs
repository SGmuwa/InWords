﻿using System.Collections.Generic;
using InWords.Auth.Extensions;
using InWords.Data.Models;
using InWords.Transfer.Data.Models;
using InWords.WebApi.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace InWords.WebApi.Controllers.v1
{
    [ApiController]
    [ApiVersion("1.0")]
    [Route("v{version:apiVersion}/[controller]")]
    public class SyncController : ControllerBase
    {
        private readonly SyncService syncService;


        public SyncController(SyncService syncService)
        {
            this.syncService = syncService;
        }

        [Route("WordPairs")]
        [HttpPost]
        //todo PushRequest (list<wordTranslation> + serverId_toDelete)
        public IActionResult PushWordPairs([FromBody] IEnumerable<WordTranslation> wordTranslationList)
        {
            //foreach (WordTranslation WordTranslation in wordTranslationList)
            //{
            //    //ServerID = 0;
            //    if (WordTranslation.ServerId == 0)
            //    {
            //        //add
            //    }
            //    else if (WordTranslation.ServerId < 0)
            //    {
            //        //delete
            //    }
            //    else
            //    {
            //        //check exist
            //    }
            //    //Server.ID = serverID
            //    //ServerID =-ID;
            //}
            return Ok();
        }

        [Authorize]
        [Route("pullWordPairs")]
        [HttpPost]
        // ReSharper disable once InconsistentNaming
        public IActionResult PullWordPairs([FromBody] IEnumerable<int> server_ids)
        {
            int authorizedId = User.GetUserId();

            PullWordsAnswer pullAnswer = syncService.PullWordPairs(authorizedId, server_ids);

            return Ok(pullAnswer);
        }
    }
}