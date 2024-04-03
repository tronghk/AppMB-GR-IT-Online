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
using AppGrIT.Entity;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExpressionController : ControllerBase
    {
        private readonly IPostExpressionss _postExpressionManager;
        private readonly IUsers _userManager;
        private readonly IToken _tokenManager;
        private readonly IPostComments _postCommentManager;
        private readonly IPosts _postManager;

        public ExpressionController(IPostExpressionss exp, IUsers userManager, IToken token, IPosts post, IPostComments postCmt)
        {

            _postExpressionManager = exp;
            _userManager = userManager;
            _tokenManager = token;
            _postManager = post;
            _postCommentManager = postCmt;
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
        public async Task<IActionResult> AddExpressionUser([FromBody] ExpressionModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var post = await _postManager.FindPostToIdAsync(model.PostId);
            if (user != null || post != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    if(model.Type.Equals("1") || model.Type.Equals("2"))
                    {
                        if (model.Type.Equals("2") && await _postCommentManager.GetCommentAsync(model.PostId, model.CommentId!) == null)
                        {
                            return NotFound(new ResponseModel
                            {
                                Status = StatusResponse.STATUS_ERROR,
                                Message = "Can not create expression because comment is null"
                            });
                        }
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
                 
                }
                return Unauthorized();
            }
            return NotFound();
        }
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        [HttpDelete("/delete-expression-post")]
        public async Task<IActionResult> DeleteExpressionUser([FromBody] ExpressionModel model)
        {
            var user = await _userManager.GetUserToUserId(model.UserId!);
            var post = await _postManager.FindPostToIdAsync(model.PostId);
            if (user != null || post != null)
            {
                var token = HttpContext.GetTokenAsync(JwtBearerDefaults.AuthenticationScheme, "access_token");
                string accesss_token = token.Result!;
                if (_tokenManager.CheckDupEmailToToken(accesss_token, user.Email))
                {
                    if (model.Type.Equals("1") || model.Type.Equals("2"))
                    {
                        
                        if (model.Type.Equals("2") && string.IsNullOrEmpty(model.CommentId))
                        {
                            return NotFound(new ResponseModel
                            {
                                Status = StatusResponse.STATUS_ERROR,
                                Message = "Can not create expression because commentId is null"
                            });
                        }
                     
                        var result = await _postExpressionManager.DeleteExpressionAsync(model);

                        if (result.Status.Equals(StatusResponse.STATUS_SUCCESS))
                        {

                            return Ok(result);
                        }
                        else
                        {
                           return BadRequest(result);
                        }
                    }

                }
                return Unauthorized();
            }
            return NotFound();
        }
    }
}
