
using AppGrIT.Authentication;
using AppGrIT.Data;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using AppGrIT.Services;
using BookManager.Model;
using Firebase.Auth;
using FirebaseAdmin.Messaging;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using System.IdentityModel.Tokens.Jwt;
using System.IO;
using System.Text.Json.Nodes;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {

        private readonly IUsers _userManager;
        private readonly IPosts _postManager;
        private readonly IToken _tokenManager;
        public UsersController(IUsers userManager, IToken tokenManager, IPosts post)
        {
            _tokenManager = tokenManager;
            _userManager = userManager;
            _postManager = post;
        }

        [HttpPost("/signup")]
        public async Task<IActionResult> Signup(SignUpModel model)
        {

            var result = await _userManager.SignUpAsync(model);
            if (!result.Status!.Equals(StatusResponse.STATUS_OK))
            {
                return BadRequest(result);
            }
            var token = await _tokenManager.GenerareTokenModel(new SignInModel
            {
                Email = model.Email,
                Password = model.Password,
            });
            return Ok(token);


        }
        [HttpPost("/signin")]
        public async Task<IActionResult> Login(SignInModel signInModel)
        {
            var result = await _userManager.SignInAsync(signInModel);
            if (result.Status!.Equals(StatusResponse.STATUS_OK))
            {
                var token = await _tokenManager.GenerareTokenModel(signInModel);
                return Ok(token);
            }
            else
            {

                return BadRequest(result);
            }
        }
        [HttpGet("/getall")]
        public async Task<IActionResult> getall(string userId)
        {

            return Ok(await _userManager.GetUserToUserId(userId));
        }
        [HttpPost("/refresh-token")]
        public async Task<IActionResult> RefreshToken(TokenModel tokenModel)
        {
            var result = await _tokenManager.CheckToken(tokenModel);
            if (result.Status!.Equals(StatusResponse.STATUS_ERROR))
            {
                return Unauthorized(result);
            }
            var claimsPrincal = _tokenManager.GetPrincipalFromExpiredToken(tokenModel.AccessToken);
            var token = await _tokenManager.RefreshToken(claimsPrincal!.Claims.ToList(), claimsPrincal.Identity!.Name!);
            return Ok(token);
        }

        [HttpGet("/reset-password")]
        public async Task<IActionResult> ForgotPassword(string email)
        {
            var result = await _userManager.ForgotPassword(email);
            if (result.Status!.Equals(StatusResponse.STATUS_OK))
            {
                return Ok(result);
            }
            return BadRequest(result);
        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPost("/verification-email")]
        public async Task<IActionResult> VerificationEmail(SignInModel model)
        {
            var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
            string accesss_token = token.Result!;
            if (_tokenManager.CheckDupEmailToToken(accesss_token, model.Email))
            {
                var result = await _userManager.VertificationEmail(model);
                if (result.Status!.Equals(StatusResponse.STATUS_OK))
                {
                    return Ok(result);
                }
                return BadRequest(result);
            }
            return Unauthorized();



        }

        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPost("/change-password")]
        public async Task<IActionResult> ChangePassword(ChangePasswordModel model)
        {

            var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
            string accesss_token = token.Result!;
            if (_tokenManager.CheckDupEmailToToken(accesss_token, model.Email))
            {
                var result = await _userManager.ChangePasswordModel(model);
                if (result.Status!.Equals(StatusResponse.STATUS_OK))
                {
                    return Ok(result);
                }
                return BadRequest(result);
            }
            return Unauthorized();




        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpGet("/user")]
        public async Task<IActionResult> GetUser(string email)
        {
            var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
            string accesss_token = token.Result!;
            if (_tokenManager.CheckDupEmailToToken(accesss_token, email))
            {
                if (await _userManager.GetUserAsync(email) != null)
                {
                    var us = await _userManager.GetInforUser(email);
                    return Ok(us);
                }
                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_NOTFOUND
                });
            }
            return Unauthorized();

        }

        [HttpPut("/edit-user")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> EditUserInfor(UserInforModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId);
            if (user != null)
            {
                var email = user.Email;
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, email))
                {
                    var result = await _userManager.EditUserInfors(model);
                    if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
                    {
                        return Ok(result);
                    }
                    return BadRequest(result);
                }
            }

            return Unauthorized();

        }

        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPost("/add-image-instead-user")]
        public async Task<IActionResult> AddImageInsteadUser(PostModel model)
        {

            var user = await _userManager.GetUserToUserId(model.UserId!);
            if (user != null )
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.CreatePostAsync(model);

                    if (result!= null)
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
