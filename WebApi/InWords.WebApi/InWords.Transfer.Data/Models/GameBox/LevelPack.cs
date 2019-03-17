﻿using System.Collections.Generic;

namespace InWords.Transfer.Data.Models.GameBox
{
    public class LevelPack
    {
        public int Level { get; set; }

        public List<WordTranslation> WordTranslations { get; set; }
    }
}