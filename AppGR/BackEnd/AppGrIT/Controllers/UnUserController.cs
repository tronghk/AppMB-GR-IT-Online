using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using AppGrIT.Services;
using AppGrIT.Services.AppGrIT.Services;
using Firebase.Auth;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UnUserController : ControllerBase
    {
        private IUnUser _unUserManager;
        private IUsers _userManager;

        public UnUserController(IUnUser pression, IUsers user)
        {
            _unUserManager = pression;
            _userManager = user;
        }
        [HttpGet("/count-unUser")]
        public async Task<IActionResult> CountUnUser(string userId)
        {
            var count = await _unUserManager.CountUnUserInAUser(userId);
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
        [HttpGet("/get-listUnUser")]
        public async Task<IActionResult> GetListUnUser(string userId)
        {
            var user = await _unUserManager.GetListUnUser(userId!);

            if (user != null)
            {
                var result = await _unUserManager.GetListUnUser(userId);
                return Ok(result);
            }
            return NotFound();
        }
        [HttpPost("/add-UnUser")]
        public async Task<IActionResult> AddUnUser([FromBody] UnUserModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var userfr = await _userManager.GetUserToUserId(model.UnUserId!);

            var us = await _unUserManager.GetUnUser(model.UserId, model.UnUserId);

            if (user != null && userfr != null && us == null)
            {
                var result = await _unUserManager.CreateUnUser(model);
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
