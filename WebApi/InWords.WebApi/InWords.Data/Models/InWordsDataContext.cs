﻿namespace InWords.Data.Models
{
    using Microsoft.EntityFrameworkCore;
    using System.Diagnostics;

    public class InWordsDataContext : DbContext
    {
        private static bool _created = false;

        public InWordsDataContext()
        {
            RecreateDb();
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionbuilder)
        {
            optionbuilder.UseSqlite(@"Data Source=App_Data\InWordsDatabase.db");
        }

        public DbSet<User> Users { get; set; }

        [Conditional("DEBUG")]
        private void RecreateDb()
        {

            if (!_created)
            {
                _created = true;
                Database.EnsureDeleted();
                Database.EnsureCreated();
            }
        }
    }
}
