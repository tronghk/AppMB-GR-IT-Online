using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Services;
using AppGrIT.Services.AppGrIT.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserFollowsController : ControllerBase
    {
        private IUserFollows _followerManager;

        public UserFollowsController(IUserFollows pression)
        {
            _followerManager = pression;
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
    }
}
