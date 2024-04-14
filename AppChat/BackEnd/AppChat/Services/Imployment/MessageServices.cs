using AppChat.Data;
using AppChat.Entity;
using AppChat.Models;
using AppGrIT.Helper;

namespace AppChat.Services.Imployment
{
    public class MessageServices : IMessages
    {
        private readonly MessageDAO _messageDao;

        public MessageServices(MessageDAO message)
        {
            _messageDao = message;
        }

        public async Task<ChatModel> CreateChatModelAsync(ChatModel model)
        {
            var chat = new ChatSegMent
            {
                UserId = model.UserId,
                UserOrtherId = model.UserId,

            };
            
            var result = await _messageDao.AddChatSegMentAsync(chat);
            if (result.Status.Equals(StatusResponse.STATUS_SUCCESS))
            {
                model.MessId = result.Message!;
                return model;
            }
            return null!;


        }
    }
}
