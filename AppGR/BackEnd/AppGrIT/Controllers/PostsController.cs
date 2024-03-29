﻿using AppGrIT.Authentication;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Reflection;
using Microsoft.AspNetCore.Components;
using AppGrIT.Models;

namespace AppGrIT.Controllers
{
    [Microsoft.AspNetCore.Components.Route("api/[controller]")]
    [ApiController]
    public class PostsController : ControllerBase
    {
        private readonly IUsers _userManager;
        private readonly IPosts _postManager;
        private readonly IToken _tokenManager;
        public PostsController(IUsers userManager, IToken tokenManager, IPosts post)
        {
            _tokenManager = tokenManager;
            _userManager = userManager;
            _postManager = post;
        }

        [HttpGet("/get-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> GetPostUser(string userId)
        {
            var user = await _userManager.GetUserToUserId(userId);
            if (user != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.GetListPostUser(userId);

                    return Ok(result);
                }
                return Unauthorized();

            }
            return NotFound();
        }
        [HttpGet("/get-expression-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> GetExpressionPost(string userId)
        {
            var user = await _userManager.GetUserToUserId(userId);
            if (user != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.GetListPostUser(userId);

                    return Ok(result);
                }
                return Unauthorized();

            }
            return NotFound();
        }
        [HttpGet("/get-avatar-user-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> GetListAvatarUser(string userId)
        {
            var user = await _userManager.GetUserToUserId(userId);
            if (user != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.GetListAvatarPostUser(userId);

                    return Ok(result);
                }

                return Unauthorized();

            }
            return NotFound();
        }

        [HttpGet("/get-cover-user-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> GetListCoverUser(string userId)
        {
            var user = await _userManager.GetUserToUserId(userId);
            if (user != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.GetListCoverPostUser(userId);

                    return Ok(result);
                }

                return Unauthorized();

            }
            return NotFound();
        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPost("/add-post-user")]
        public async Task<IActionResult> AddPostUser([FromBody] PostModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            if (user != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.CreatePostAsync(model);

                    if (result != null)
                    {

                        return Ok(result);
                    }
                    else
                    {
                        BadRequest(new ResponseModel
                        {
                            Status = StatusResponse.STATUS_ERROR,
                            Message = MessageResponse.MESSAGE_CREATE_FAIL
                        });
                    }
                }
                return Unauthorized();

            }
            return NotFound();
        }
    }
}
