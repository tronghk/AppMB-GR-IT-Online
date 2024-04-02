﻿using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Services;
using AppGrIT.Services.AppGrIT.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using AppGrIT.Models;

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
        public async Task<IActionResult> CountFriends(string userId)
        {
            var count = await _friendsManager.CountFriendsInAUser(userId);
            // Kết quả ra 0 thì trả về BadRequest
            if (count == 0)
            {

                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_NOTFOUND
                }
                );
            }
            return Ok(count);
        }
        [HttpGet("/get-listUserFriend")]
        public async Task<IActionResult> GetListUserFriend(string userId)
        {
            var user = await _friendsManager.GetListUserFriends(userId!);

            if (user != null)
            {             
                    var result = await _friendsManager.GetListUserFriends(userId);
                    return Ok(result);                          
            }
            return NotFound();
        }
        [HttpPost("/add-friend")]
        public async Task<IActionResult> AddFriendUser([FromBody] UserFriendsModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var userfr = await _userManager.GetUserToUserId(model.UserFriendId!);
            
            var us = await _friendsManager.GetUserFriend(model.UserId, model.UserFriendId);

            if (user != null && userfr != null && us == null)
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
    }
}
