using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Services;
using AppGrIT.Services.AppGrIT.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserFollowsController : ControllerBase
    {
        private IUserFollows _followerManager;
        private readonly IUsers _userManager;

        public UserFollowsController(IUserFollows pression, IUsers userManager)
        {
            _followerManager = pression;
            _userManager = userManager;
        }
        [HttpGet("/count-followers")]
        
        public async Task<IActionResult> CountFollowers(string userId)
        {
            var count = await _followerManager.CountFollowersInAUser(userId);
            Dictionary<string, string> dic = new Dictionary<string, string>();
            dic.Add("count", count.ToString());
            return Ok(dic);

            
        }
        [HttpGet("/count-user-followers")]
        public async Task<IActionResult> CountUserFollowers(string userId)
        {
            var count = await _followerManager.CountFollowersInAUser(userId);
            Dictionary<string, string> dic = new Dictionary<string, string>();
            dic.Add("count", count.ToString());
            return Ok(dic);


        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpGet("/get-user-follow")]
        public async Task<IActionResult> GetListUserFollow()
        {
            var identity = HttpContext.User.Identity as ClaimsIdentity;
            var userId = identity.FindFirst("userId")!.Value;


            var user = await _userManager.GetUserToUserId(userId!);

            if (user != null)
            {

                var result = await _followerManager.GetListUserIsFollow(userId);
                return Ok(result);



            }
            return NotFound();
        }
    }
}
