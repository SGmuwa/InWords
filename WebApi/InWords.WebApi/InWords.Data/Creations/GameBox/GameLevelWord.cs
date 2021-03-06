﻿using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using InWords.Data.Domains;

namespace InWords.Data.Creations.GameBox
{
    public class GameLevelWord
    {
        [Key] public int GameLevelWordId { get; set; }

        public int GameLevelId { get; set; }

        [ForeignKey(nameof(GameLevelId))] public virtual GameLevel GameLevel { get; set; }

        public int WordPairId { get; set; }

        [ForeignKey(nameof(WordPairId))] public virtual WordPair WordPair { get; set; }
    }
}