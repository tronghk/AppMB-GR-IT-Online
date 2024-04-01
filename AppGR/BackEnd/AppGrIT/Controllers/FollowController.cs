using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using AppGrIT.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using AppGrIT.Authentication;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FollowController : ControllerBase
    {
        private readonly IFollows _followManager;
        private readonly IUsers _userManager;
        private readonly IPosts _postManager;
        private readonly IToken _tokenManager;
        public FollowController(IFollows followManager, IToken token, IUsers user)
        {
            _followManager = followManager;
            _userManager = user;
            _tokenManager = token;
        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPost("/add-follow")]
        public async Task<IActionResult> AddPostCommentUser([FromBody] UserFollowsModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var userfl = await _userManager.GetUserToUserId(model.UserFollowId!);
            var us = await _followManager.GetUserFollow(model.UserId,model.UserFollowId);

            if (user != null && userfl != null && us == null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {

                    var result = await _followManager.CreateUserFollow(model);
                    if(result!= null)
                    {
                        return Ok(result);
                    }
                    return
                        BadRequest(new ResponseModel
                        {
                            Status = StatusResponse.STATUS_ERROR,
                            Message = MessageResponse.MESSAGE_CREATE_FAIL
                        });
                    
                }
                return Unauthorized();
            }
            return NotFound();
        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpDelete("/delete-follow")]
        public async Task<IActionResult> DeletePostCommentUser([FromBody] UserFollowsModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var userfl = await _userManager.GetUserToUserId(model.UserFollowId!);
            var us = await _followManager.GetUserFollow(model.UserId, model.UserFollowId);
            if (user != null && userfl != null && us != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {

                    var result = await _followManager.DeleteUserFollow(model);
                    if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
                    {
                        return Ok(result);
                    }
                    return
                        BadRequest(result);

                }
                return Unauthorized();
            }
            return NotFound();
        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpGet("/get-follow")]
        public async Task<IActionResult> DeletePostCommentUser(string userId)
        {
            var user = await _userManager.GetUserToUserId(userId!);
           
            if (user != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {

                    var result = await _followManager.GetListUserFollow(userId);
                   return Ok(result);

                }
                return Unauthorized();
            }
            return NotFound();
        }
    }
}
