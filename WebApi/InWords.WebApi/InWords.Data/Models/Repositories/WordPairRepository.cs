﻿namespace InWords.Data.Models.Repositories
{
    using System;
    using System.Collections.Generic;
    using System.Text;
    using System.Threading.Tasks;
    using Microsoft.EntityFrameworkCore;

    public class WordPairRepository : Repository<WordPair>
    {
        private readonly WordRepository wordRepository = null;

        public WordPairRepository(DbContext context) : base(context)
        {
            wordRepository = new WordRepository(context);
        }

        public async Task<WordPair> Stack(Word first, Word second)
        {
            first = await wordRepository.Stack(first);
            second = await wordRepository.Stack(second);
            WordPair result = new WordPair()
            {
                WordForeignID = first.WordID,
                WordNativeID = second.WordID,
            };

            return await Stack(result);
        }

        public async Task<WordPair> Stack(WordPair pair)
        {
            return await Stack(pair, words =>
            words.WordForeignID == pair.WordForeignID
            && words.WordNativeID == pair.WordNativeID
            || words.WordForeignID == pair.WordNativeID
            && words.WordNativeID == pair.WordForeignID
            );
        }
    }
}
