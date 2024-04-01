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
    public class UnUserController : ControllerBase
    {
        private IUnUser _unUserManager;

        public UnUserController(IUnUser pression)
        {
            _unUserManager = pression;
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
    }
}
