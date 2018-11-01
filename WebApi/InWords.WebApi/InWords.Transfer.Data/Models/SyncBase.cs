﻿namespace InWords.Transfer.Data
{
    using System;
    
    /// <summary>
    /// Transfer response pack: Id and ServerId
    /// </summary>
    public class SyncBase : ICloneable
    {
        public int Id { get; set; }

        public int ServerId { get; set; }

        #region Ctor
        /// <summary>
        /// Constructor part
        /// </summary>
        public SyncBase() { }

        public SyncBase(int serverID)
        {
            ServerId = serverID;
        }

        public SyncBase(int serverID, int onClientID)
        {
            Id = onClientID;
            ServerId = serverID;
        }


        public SyncBase(SyncBase wordTranslationBase)
        {
            Id = wordTranslationBase.Id;
            ServerId = wordTranslationBase.Id;
        }
        #endregion

        object ICloneable.Clone()
        {
            return new SyncBase(this);
        }
    }
}