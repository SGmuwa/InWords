﻿using System;
using System.Collections.Generic;
using System.Text;

namespace InWords.Auth.TFA.Models
{
    public class EmailConfig
    {
        public string Login { get; set; }
        public string Password { get; set; }
        public string SMTPserver { get; set; }
        public int Port { get; set; }
    }
}
