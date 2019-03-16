﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using InWords.Auth.Extensions;
using InWords.Auth.Models;
using InWords.Data.Enums;
using InWords.Data.Models.InWords.Domains;
using InWords.Data.Models.InWords.Repositories;
using InWords.Service.Encryption;
using InWords.Service.Encryption.Interfaces;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;

namespace InWords.WebApi.Providers
{
    public class AccountIdentityProvider
    {
        public readonly AccountRepository AccountRepository;

        private readonly ILogger logger;
        private readonly IPasswordSalter passwordSalter;

        /// <summary>
        ///     Create provider via repository
        /// </summary>
        /// <param name="repository"></param>
        public AccountIdentityProvider(AccountRepository repository, ILogger logger)
        {
            this.logger = logger;
            AccountRepository = repository;
            passwordSalter = new SaltManager();
        }

        /// <summary>
        ///     This is to check user identity from [Request]
        /// </summary>
        /// <returns>null or ClaimsIdentity</returns>
        public TokenResponse GetIdentity(HttpRequest request)
        {
            BasicAuthClaims x = request.GetBasicAuthorizationCalms();

            if (x != null)
            {
                logger.Log(LogLevel.Information, "#GetIdentity {0}", x.Email, x.Password);
                TokenResponse response = GetIdentity(x.Email, x.Password);
                return response;
            }

            logger.Log(LogLevel.Error, $"Identity lost on Request {request.Headers}");
            return null;
        }

        /// <summary>
        ///     This is to check user identity [FromBody]
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        public TokenResponse GetIdentity(BasicAuthClaims user)
        {
            return GetIdentity(user.Email, user.Password);
        }

        /// <summary>
        ///     This is to check user identity
        /// </summary>
        /// <param name="email"></param>
        /// <param name="password"></param>
        /// <returns></returns>
        /// <exception cref="ArgumentException"></exception>
        public TokenResponse GetIdentity(string email, string password)
        {
            Account account = AccountRepository.Get(x => x.Email == email).SingleOrDefault();
            if (account == null)
                throw new ArgumentNullException($"Email not found {email}");

            bool isValidPassword = passwordSalter.EqualsSequence(password, account.Hash);

            if (!isValidPassword)
                throw new ArgumentException("Invalid password");

            IEnumerable<Claim> claims = null;

            var nameId = "-1";
            nameId = account.AccountId.ToString();
            email = account.Email;
            string defaultRole = account.Role.ToString();

            claims = new List<Claim>
            {
                new Claim(ClaimTypes.NameIdentifier, nameId),
                new Claim(ClaimTypes.Email, email),
                new Claim(ClaimsIdentity.DefaultRoleClaimType, defaultRole)
            };

            var claimsIdentity = new ClaimsIdentity(claims);

            var response = new TokenResponse(claimsIdentity);

            return response;
        }

        public async Task<Account> CreateUserAccount(string email, string password)
        {
            byte[] saltedKey = passwordSalter.SaltPassword(password);
            var newAccount = new Account
            {
                Email = email,
                Hash = saltedKey,
                Role = RoleType.User,
                RegistrationDate = DateTime.Now,
                User = null
            };

            newAccount.User = new User
            {
                NickName = "YourNick",
                Experience = 0
            };

            await AccountRepository.Create(newAccount);
            //await Update(newAccount);

            return newAccount;
        }
    }
}