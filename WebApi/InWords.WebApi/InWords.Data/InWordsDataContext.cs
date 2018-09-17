﻿namespace InWords.Data
{
    using InWords.Data.Enums;
    using InWords.Data.Models;
    using Microsoft.EntityFrameworkCore;
    using System;
    using System.Diagnostics;

    public class InWordsDataContext : DbContext
    {
        private static bool _created = false;

        public InWordsDataContext()
        {
            RecreateDb();
            Database.EnsureCreated();
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionbuilder)
        {
            optionbuilder.UseSqlite(@"Data Source=InWordsDatabase.db");
        }

        public DbSet<User> Users { get; set; }

        public DbSet<Account> Accounts { get; set; }

        private void RecreateDb()
        {

            if (!_created)
            {
                _created = true;
                Database.EnsureDeleted();
                Database.EnsureCreated();
                Accounts.Add(new Account() { Email = "admin@gmail.com", Password = "1234", Role = RoleType.Admin,RegistrationDate = DateTime.Now});
                Accounts.Add(new Account() { Email = "user@gmail.com", Password = "1234", Role = RoleType.User, RegistrationDate = DateTime.Now });
                SaveChanges();
            }
        }
    }
}
