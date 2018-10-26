﻿namespace InWords.Data.Models
{
    using InWords.Data.Enums;
    using System;
    using System.ComponentModel.DataAnnotations;

    public class Account
    {
        [Key]
        public int AccountID { get; set; }

        [Required]
        [EmailAddress]
        [StringLength(64)]
        public string Email { get; set; }

        [Required]
        [DataType(DataType.Password)]
        [StringLength(32)]
        public string Password { get; set; }

        [Required]
        public RoleType Role { get; set; }

        [Required]
        public DateTime RegistrationDate { get; set; }

        [Required]
        public virtual User User { get; set; }
    }
}
