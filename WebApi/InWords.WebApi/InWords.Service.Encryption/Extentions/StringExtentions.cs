﻿namespace InWords.Service.Encryption
{
    using System.Text;

    public static class StringExtentions
    {
        /// <summary>
        /// Get byte[] from UTF8 String
        /// </summary>
        /// <param name="password"></param>
        /// <returns></returns>
        public static byte[] ToByteArray(this string password)
        {
            return Encoding.UTF8.GetBytes(password);
        }

        public static string ToStringFromByte(this byte[] byteArray)
        {
            return Encoding.UTF8.GetString(byteArray);
        }
    }
}