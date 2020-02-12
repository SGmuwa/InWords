﻿using MediatR;

namespace InWords.WebApi.Services.Abstractions
{
    public class RequestObject<TRequest, TReply> : IRequest<TReply> where TRequest : new() where TReply : new()
    {
        public TRequest Value { get; set; }
        public RequestObject(TRequest value)
        {
            this.Value = value;
        }
    }
}
