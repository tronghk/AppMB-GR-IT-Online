using AppChat.Entity;
using AppGrIT.Data;
using AppGrIT.Helper;
using AppGrIT.Model;
using FireSharp.Response;

namespace AppChat.Data
{
    public class MessageDAO
    {
        private readonly ConnectFirebase _firebase;
        public MessageDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<ResponseModel> AddChatSegMentAsync(ChatSegMent chat)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("ChatSegMent/", chat);

                chat.MessId = response.Result.name;
                SetResponse setResponse = await _firebase._client.SetAsync("ChatSegMent/" + chat.MessId, chat);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = chat.MessId

                };
            }
            catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = ex.Message
                };
            }


        }
    }
}
