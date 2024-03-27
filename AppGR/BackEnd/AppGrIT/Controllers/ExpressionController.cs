using AppGrIT.Helper;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using AppGrIT.Services;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExpressionController : ControllerBase
    {
        private readonly IPostExpressionss _postExpressionManager;

        public ExpressionController(IPostExpressionss exp) {

            _postExpressionManager = exp;
        }
        [HttpGet("/get-post-expression")]
        public async Task<IActionResult> GetListCoverUser(string postId)
        {
                    var result = await _postExpressionManager.GetPostExpression(postId);
            if (result.Count > 0)
            {
                return Ok(result);
            }
            return NotFound();
        }
    }
}
