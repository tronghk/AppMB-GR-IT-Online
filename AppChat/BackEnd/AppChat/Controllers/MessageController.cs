using AppChat.Models;
using AppChat.Services;
using AppGrIT.Helper;
using AppGrIT.Model;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace AppChat.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MessageController : ControllerBase
    {
        private readonly IMessages _messageManager;

        public MessageController(IMessages messages) {
        
            _messageManager = messages;
        }

        [HttpPost("/create-chat-message")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> CreateChat([FromBody] ChatModel model)
        {
            // kiem tra phai3 co tk va trung lap tk 
            var result = await _messageManager.CreateChatModelAsync(model);
            if(result!= null)
            {
                return Ok(result);
            }
            
            return BadRequest(new ResponseModel {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            });
        }
    }
}
