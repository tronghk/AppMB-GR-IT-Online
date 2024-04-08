using AppGrIT.Authentication;
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
using Firebase.Auth;

namespace AppGrIT.Controllers
{
    [Microsoft.AspNetCore.Components.Route("api/[controller]")]
    [ApiController]
    public class PostsController : ControllerBase
    {
        private readonly IUsers _userManager;
        private readonly IPosts _postManager;
        private readonly IImages _imageManager;
        private readonly IToken _tokenManager;
        public PostsController(IUsers userManager, IToken tokenManager, IPosts post, IImages image)
        {
            _tokenManager = tokenManager;
            _userManager = userManager;
            _postManager = post;
            _imageManager = image;
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
        [HttpGet("/get-post-self")]
       
        public async Task<IActionResult> GetPostSelfUser(string userId)
        {
            
              var result = await _postManager.GetListPostSelfUser(userId);
               return Ok(result);
        }
        [HttpGet("/get-one-post")]
        public async Task<IActionResult> GetPostFromPostId(string postId)
        {
            var post = await _postManager.FindPostToIdAsync(postId);
            if(post != null && post.PostType!.Equals("1"))
            {
                return Ok(post);
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
            model.PostType = "1";
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
        [HttpPut("/edit-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> EditPost(PostModel model)
        {

            model.PostType = "1";
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var post = await _postManager.FindPostToIdAsync(model.PostId!);
            if (user != null && post != null && post.UserId!.Equals(user.UserId))
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postManager.EditPostAsync(model);

                    if (result != null)
                    {

                        return Ok(result);
                    }
                    else
                    {
                        BadRequest(new ResponseModel
                        {
                            Status = StatusResponse.STATUS_ERROR,
                            Message = MessageResponse.MESSAGE_UPDATE_FAIL
                        });
                    }
                }
                return Unauthorized();

            }
            return NotFound();
        }
        [HttpDelete("/delete-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> DeletePost(string postId, string userId)
        {

            
            var user = await _userManager.GetUserToUserId(userId);
            var post = await _postManager.FindPostToIdAsync(postId);
            if (user != null &&  post != null && post.UserId!.Equals(userId))
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    
                    var result = await _postManager.DeletePostAsync(postId);

                    if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
                    {

                        return Ok(result);
                    }
                    else
                    {
                        BadRequest(result);
                    }
                }
                return Unauthorized();

            }
            return NotFound();
        }
        [HttpPost("/share-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> SharePost(SharePostModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId);
            var post = await _postManager.FindPostToIdAsync(model.PostId);
            var postShare =  await _postManager.GetPostShare(model.PostId, model.UserId);
            if (user != null && post != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    if(postShare != null)
                    {
                        return BadRequest(new ResponseModel
                        {
                            Status = StatusResponse.STATUS_ERROR,
                            Message = "Can not share post because post was shared"
                        });
                    }

                    var result = await _postManager.SharePost(model);

                    if (result != null)
                    {

                        return Ok(result);
                    }
                    else
                    {
                        BadRequest(new ResponseModel
                        {
                            Status = StatusResponse.STATUS_ERROR,
                            Message = "Can not shared"
                        });
                    }
                }
                return Unauthorized();

            }
            return NotFound();
        }
        [HttpPost("/delete-share-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> DeleteSharePost(SharePostModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId);
            var post = await _postManager.FindPostToIdAsync(model.PostId);
            var postShare = await _postManager.GetPostShare(model.PostId, model.UserId);
            if (user != null && post != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    if (postShare == null)
                    {
                        return BadRequest(new ResponseModel
                        {
                            Status = StatusResponse.STATUS_ERROR,
                            Message = "Can not delete because post wasn't able share"
                        });
                    }

                    var result = await _postManager.DeleteSharePostAsync(model);

                    if (result.Status.Equals(StatusResponse.STATUS_SUCCESS))
                    {

                        return Ok(result);
                    }
                    else
                    {
                        BadRequest(result);
                    }
                }
                return Unauthorized();

            }
            return NotFound();
        }
        [HttpGet("/get-share-post")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> GetSharePost(string userId)
        {
            var user = await _userManager.GetUserToUserId(userId);
           
           
            if (user != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    

                    var result = await _postManager.GetListPostShared(userId);

                    if (result.Count>0)
                    {

                        return Ok(result);
                    }
                    else
                    {
                        NotFound();
                    }
                }
                return Unauthorized();

            }
            return NotFound();
        }

        [HttpGet("/FindPostByContent")]
        public async Task<IActionResult> FindPostByContent(string Content)
        {
            var user = await _postManager.FindPostByContent(Content!);

            if (user != null)
            {
                var result = await _postManager.FindPostByContent(Content);
                return Ok(result);
            }
            return NotFound();
        }


    }
}
