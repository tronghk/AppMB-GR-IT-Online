using AppGrIT.Helper;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using AppGrIT.Services;
using AppGrIT.Model;
using AppGrIT.Models;
using AppGrIT.Authentication;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExpressionController : ControllerBase
    {
        private readonly IPostExpressionss _postExpressionManager;
        private readonly IUsers _userManager;
        private readonly IToken _tokenManager;

        public ExpressionController(IPostExpressionss exp, IUsers userManager, IToken token )
        {

            _postExpressionManager = exp;
            _userManager = userManager;
            _tokenManager = token;
        }
        [HttpGet("/get-post-expression")]
        public async Task<IActionResult> GetExpressionPost(string postId)
        {
            var result = await _postExpressionManager.GetPostExpression(postId);
            if (result.Count > 0)
            {
                return Ok(result);
            }
            return NotFound();
        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpPost("/add-expression-post")]
        public async Task<IActionResult> AddPostCommentUser([FromBody] ExpressionModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            if (user != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    var result = await _postExpressionManager.CreateExpressionAsync(model);

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
    }
}
