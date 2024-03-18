﻿
using AppGrIT.Models;
using BookManager.Model;
using FirebaseAdmin;
using FirebaseAdmin.Auth;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.Extensions.Options;
using System.Security.Claims;
using System.Text.Encodings.Web;

namespace AppGrIT.Authentication
{
    public interface IToken
    {
        public Task<TokenModel> GenerareTokenModel(SignInModel model);
    }
}
