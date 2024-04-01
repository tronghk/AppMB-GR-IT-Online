using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Services;
using AppGrIT.Services.AppGrIT.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FollowersController : ControllerBase
    {
        private IFollowers _followerManager;

        public FollowersController(IFollowers pression)
        {
            _followerManager = pression;
        }
        [HttpGet("/count-followers")]
        public async Task<IActionResult> CountFollowers(string userId)
        {
            var count = await _followerManager.CountFollowersInAUser(userId);
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
    }
}
