
using AppGrIT.Authentication;
using AppGrIT.Data;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using AppGrIT.Services;
using AppGrIT.Services.Imployement;
using BookManager.Model;
using Firebase.Auth;
using FirebaseAdmin.Auth;
using FirebaseAdmin.Messaging;
using Google.Api.Gax.Rest;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System.IdentityModel.Tokens.Jwt;
using System.IO;
using System.Reflection;
using System.Text.Json.Nodes;

namespace AppGrIT.Controllers
{
    [Microsoft.AspNetCore.Components.Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {

        private readonly IUsers _userManager;
        private readonly IToken _tokenManager;
        private readonly IPosts _postManager;
        private readonly IImages _imageManager;

        public UsersController(IUsers userManager, IToken tokenManager, IPosts postManager, IImages image)
        {
            _tokenManager = tokenManager;
            _userManager = userManager;
            _postManager = postManager;
            _imageManager = image;
        }

        [HttpPost("/signup")]
        public async Task<IActionResult> Signup(SignUpModel model)
        {

            var result = await _userManager.SignUpAsync(model);
            if (!result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
            {
                return BadRequest(result);
            }
            var user = await _userManager.GetUserAsync(model.Email);

            //tao post mac dinh
            var im = await _imageManager.GetLinkAvatarDefault();
            ImagePostModel i = new ImagePostModel
            {
                ImagePath = im,
                ImageContent = "Default"
            };
            List<ImagePostModel> list = new List<ImagePostModel>();
            list.Add(i);
            var post = new PostModel
            {
                PostType = "3",
                UserId = user.UserId,
                PostTime = DateTime.Now,
                imagePost = list,
            };
            var postInstes = await _postManager.CreatePostAsync(post);
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
            if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
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
            if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
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
                if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
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
                if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
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
       
        [HttpPost("/sign-in-google")]
        public async Task<IActionResult> SignInGoogle(string idToken)
        {
            var result = await _userManager.SignInGoogleAsync(idToken);
            if(result.Status == StatusResponse.STATUS_SUCCESS)
            {
                var link = result.Message;
                var email = await _userManager.GetEmailModelFromLink(link!);
                var account = await _userManager.GetUserAsync(email);
                if(account == null)
                {
                    var response = await _userManager.SignUpGoogleAsync(link!);
                    var user = await _userManager.GetUserAsync(email);

                    //tao post mac dinh
                    var im = await _imageManager.GetLinkAvatarDefault();
                    ImagePostModel i = new ImagePostModel
                    {
                        ImagePath = im,
                        ImageContent = "Default"
                    };
                    List<ImagePostModel> list = new List<ImagePostModel>();
                    list.Add(i);
                    var post = new PostModel
                    {
                        PostType = "3",
                        UserId = user.UserId,
                        PostTime = DateTime.Now,
                        imagePost = list,
                    };
                    var postInstes = await _postManager.CreatePostAsync(post);
                }
                var token = await _tokenManager.GenerareTokenModel(new SignInModel
                {
                    Email = email
                });
                return Ok(token);


            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_LOGIN_FAIL
            });
        }

        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPost("/add-image-instead-user")]
        public async Task<IActionResult> AddImageInsteadUser([FromBody]PostModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            model.PostType = "3";
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
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPut("/edit-image-instead-user")]
        public async Task<IActionResult> EditImageInsteadUser([FromBody] PostModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            model.PostType = "3";
            if (user != null && await _postManager.FindPostToIdAsync(model.PostId!) != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.EditImagePostInstead(model);

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
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPost("/add-image-cover-user")]
        public async Task<IActionResult> AddImageCoverUser([FromBody] PostModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            model.PostType = "2";
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
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPut("/edit-image-cover-user")]
        public async Task<IActionResult> EditImageCoverUser([FromBody] PostModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            model.PostType = "2";
            if (user != null && await _postManager.FindPostToIdAsync(model.PostId) != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.EditImagePostInstead(model);

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
        [HttpGet("/get-user-id")]
        public async Task<IActionResult> GetUserBasic(string userId)
        {
            var user = await _userManager.GetUserToUserId(userId!);
            if (user != null)
            {

                var result = await _userManager.GetInfoUser(user.UserId);
                return Ok(result);

            }
            return NotFound("Can not find user");
        }
        [HttpGet("/FindUserByLastName")]
        public async Task<IActionResult> FindUserByLastName(string LastName)
        {
            var result = await _userManager.FindUserByLastName(LastName);

            if (result != null)
            {
               
                return Ok(result);
            }
            return NotFound();
        }

        [HttpGet("/FindUserByAddress")]
        public async Task<IActionResult> FindUserByAddress(string Address)
        {
            var user = await _userManager.FindUserByAddress(Address);

            if (user != null)
            {
               
                return Ok(user);
            }
            return NotFound();
        }
        [HttpGet("/FindUserByAge")]
        public async Task<IActionResult> FindUserByAge(int age)
        {
            var result = await _userManager.FindUserByAge(age);

            if (result != null)
            {
                
                return Ok(result);
            }
            return NotFound();
        }
        [HttpGet("/FindUserByLastName_Address_Age")]
        public async Task<IActionResult> FindUserByLastName_Address_Age(string input)
        {
            var users = await _userManager.FindUserByLastName_Address_Age(input);

            if (users != null && users.Any())
            {
                return Ok(users);
            }
            return NotFound();
        }


    }


}
