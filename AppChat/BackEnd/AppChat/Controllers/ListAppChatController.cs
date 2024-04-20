using AppChat.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace AppChat.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ListAppChatController : ControllerBase
    {
        private readonly IListAppChat _ListManager;

        public ListAppChatController(IListAppChat listchat)
        {

            _ListManager = listchat;
        }

        [HttpGet("/get-listMessOrtherUser")]
        public async Task<IActionResult> GetListMessOrtherUser(string userId)
        {
            var result = await _ListManager.GetListMess(userId);

            if (result != null)
            {               
                return Ok(result);
            }
            return NotFound();
        }
        [HttpGet("/get-listDetailsChat")]
        public async Task<IActionResult> GetListDetailsChat(string chatId)
        {
            var result = await _ListManager.GetListDetailsChat(chatId);

            if (result != null)
            {
                return Ok(result);
            }
            return NotFound();
        }
    }
}
