using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Services;
using AppGrIT.Services.AppGrIT.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using AppGrIT.Models;
using Microsoft.AspNetCore.Authorization;
using System.Reflection;
using System.Security.Claims;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserFriendsController : ControllerBase
    {
        private IUserFriends _friendsManager;
        private IUsers _userManager;
        

        public UserFriendsController(IUserFriends pression, IUsers user)
        {
            _friendsManager = pression;
            _userManager = user;
        }
        [HttpGet("/count-userFriend")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> CountFriends(string userId)
        {
            var count = await _friendsManager.CountFriendsInAUser(userId);
            Dictionary<string, string> dic = new Dictionary<string, string>();
            dic.Add("count", count.ToString());
            return Ok(dic);
        }
        [HttpGet("/get-listUserFriend")]
        public async Task<IActionResult> GetListUserFriend(string userId)
        {
            var user = await _userManager.GetUserToUserId(userId);

            if (user != null)
            {             
                    var result = await _friendsManager.GetListUserFriends(userId);
                    return Ok(result);                          
            }
            return NotFound();
        }
        [HttpGet("/get-user-add-friend")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> GetUserAddFriend()
        {
            var identity = HttpContext.User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var userId = identity.FindFirst("userId")!.Value;
                var user = await _userManager.GetUserToUserId(userId);

                if (user != null)
                {
                    var result = await _friendsManager.GetListUserAddFriends(userId);
                    List<UserModel> list = new List<UserModel>();
                    foreach (var item in result)
                    {
                       var a = await _userManager.GetInfoUser(item.UserFriendId);
                        list.Add(a);
                    }

                    return Ok(list);
                }
            }
           
            return NotFound();
        }
        [HttpPost("/add-friend")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> AddFriendUser([FromBody] UserFriendsModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var userfr = await _userManager.GetUserToUserId(model.UserFriendId!);
            
            var us = await _friendsManager.GetUserFriend(model.UserId, model.UserFriendId);
            if(us != null)
            {
                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Duplicate user"
                });
            }
            if (user != null && userfr != null)
            {
                var result = await _friendsManager.CreateUserFriend(model);
                if (result != null)
                {
                    return Ok(result);
                }
                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_CREATE_FAIL
                });
            }
            return NotFound();
        }
        [HttpPut("/update-friend")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> UpdateFriendUser([FromBody] UserFriendsModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var userfr = await _userManager.GetUserToUserId(model.UserFriendId!);

            var us = await _friendsManager.GetUserFriend(model.UserId, model.UserFriendId);
           
            if (user != null && userfr != null && us != null)
            {
                var result = await _friendsManager.UpdateUserFriend(model);
                if (result != null)
                {
                    return Ok(result);
                }
                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_CREATE_FAIL
                });
            }
            return NotFound();
        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpDelete("/delete-friend")]
        public async Task<IActionResult> DeleteUserFriend([FromBody] UserFriendsModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var userfl = await _userManager.GetUserToUserId(model.UserFriendId!);
            var us = await _friendsManager.GetUserFriend(model.UserId, model.UserFriendId);
            if (user != null && userfl != null && us != null)
            {
                var result = await _friendsManager.DeleteUserFriend(model);
                if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
                {
                    return Ok(result);
                }
                return
                    BadRequest(result);

            }
            return NotFound();
        }
    }
}
