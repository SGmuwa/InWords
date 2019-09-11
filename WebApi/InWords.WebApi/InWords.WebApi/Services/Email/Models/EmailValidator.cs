﻿using InWords.Data.Repositories;
using InWords.WebApi.Services.Email.Abstractions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace InWords.WebApi.Services.Email.Models
{
    public class EmailCodeValidator : IValidator
    {
        public readonly EmailVerifierRepository emailVerifierRepository = null;
        public EmailCodeValidator(EmailVerifierRepository emailVerifierRepository)
        {
            this.emailVerifierRepository = emailVerifierRepository;
        }

        bool IValidator.IsValid()
        {
            throw new NotImplementedException();
        }
    }
}