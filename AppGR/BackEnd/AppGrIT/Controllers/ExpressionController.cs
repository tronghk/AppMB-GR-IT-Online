using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExpressionController : ControllerBase
    {
        private readonly IExpression _expressionManager;

        public ExpressionController(IExpression pression) {
            _expressionManager = pression;
        }
        [HttpGet("/count-expression-type")]
        public async Task<IActionResult> CountExpressionInPost(string postId, string expression)
        {
            var count = await _expressionManager.CountExpressionInPostFromType(postId, expression);
            if (count == 0)
                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_NOTFOUND
                });
            return Ok(count);
        }
    }
}
