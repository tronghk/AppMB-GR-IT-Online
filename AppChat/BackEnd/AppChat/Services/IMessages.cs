using AppChat.Models;

namespace AppChat.Services
{
    public interface IMessages
    {
        public Task<ChatModel>  CreateChatModelAsync(ChatModel model);
    }
}
