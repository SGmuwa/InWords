﻿using System;
using System.Collections.Generic;
using System.Text;

namespace InWords.Transfer.Data.Models
{
    public class PullWordsAnswer
    {
        public List<int> RemovedServerIds;

        public List<WordTranslation> AddedWords;
    }
}