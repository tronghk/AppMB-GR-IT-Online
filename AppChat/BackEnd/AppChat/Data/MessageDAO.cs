using AppChat.Entity;
using AppGrIT.Data;
using AppGrIT.Helper;
using AppGrIT.Model;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System;

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

        public async Task<ResponseModel> AddMessageAsync(DetailsChat chat)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("DetailsChat/", chat);

                chat.DetailId = response.Result.name;
                SetResponse setResponse = await _firebase._client.SetAsync("DetailsChat/" + chat.DetailId, chat);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = chat.DetailId

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
        public async Task<ResponseModel> AddGroupChatAsync(GroupChat chat)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("GroupChat/", chat);

                chat.GroupId = response.Result.name;
                SetResponse setResponse = await _firebase._client.SetAsync("GroupChat/" + chat.GroupId, chat);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = chat.GroupId

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
        public async Task<ResponseModel> UpdateGroupChatAsync(GroupChat chat)
        {
            try
            {
                SetResponse setResponse = await _firebase._client.SetAsync("GroupChat/" + chat.GroupId, chat);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = chat.GroupId

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
        public async Task<ResponseModel> AddMemberGroupChatAsync(GroupMember member)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("GroupMember/", member);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = MessageResponse.MESSAGE_CREATE_SUCCESS

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

        public async Task<bool> CheckUserValid(string userId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Users");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
          
           if(jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var key = item.Key!.ToString();
                    if (key.Equals(userId))
                    {
                        return true;
                    }
                }
            }
            return false;

        }

        public async Task<bool> CheckChatExist(string chatId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("ChatSegMent");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

           if(jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var key = item.Key!.ToString();
                    if (key.Equals(chatId))
                    {
                        return true;
                    }
                }
            }
            return false;

        }
        public async Task<bool> CheckChatGrExist(string chatId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("GroupChat");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

           if(jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var key = item.Key!.ToString();
                    if (key.Equals(chatId))
                    {
                        return true;
                    }
                }
            }
            return false;

        }
        public async Task<string> DeleteChatSegMentAsync(string chatId)
        {
            try
            {
                await _firebase._client.DeleteAsync("ChatSegMent/" + chatId);
                return MessageResponse.MESSAGE_DELETE_SUCCESS;
            }
            catch
            {
                return MessageResponse.MESSAGE_DELETE_FAIL;
            }


        }
        public async Task<string> DeleteGroupChatAsync(string groupId)
        {
            try
            {
                await _firebase._client.DeleteAsync("GroupChat/" + groupId);
                return MessageResponse.MESSAGE_DELETE_SUCCESS;
            }
            catch
            {
                return MessageResponse.MESSAGE_DELETE_FAIL;
            }


        }
        public async Task<bool> DeleteDetailChatSegMentAsync(string chatId)
        {
            try
            {
                FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("DetailsChat");
                JObject jsonResponse = firebaseResponse.ResultAs<JObject>();


                if (jsonResponse != null)
                {
                    // lọc bài viết mới
                    foreach (var item in jsonResponse)
                    {
                        var value = item.Value!.ToString();
                        //path Object
                        var mess = JsonConvert.DeserializeObject<DetailsChat>(value);
                        if (mess.ChatId == chatId)
                        {
                            await _firebase._client.DeleteAsync("DetailsChat/" + mess.DetailId);
                        }

                    }
                    return true;
                }
                return false;
            }catch(Exception ex)
            {
                return false;
            }


        }
        public async Task<bool> DeleteMemberGroupAsync(string groupId)
        {
            try
            {
                FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("GroupMember");
                JObject jsonResponse = firebaseResponse.ResultAs<JObject>();


                if (jsonResponse != null)
                {
                    // lọc bài viết mới
                    foreach (var item in jsonResponse)
                    {
                        var value = item.Value!.ToString();
                        //path Object
                        var mess = JsonConvert.DeserializeObject<GroupMember>(value);
                        if (mess.GroupId == groupId)
                        {
                            await _firebase._client.DeleteAsync("GroupMember/" + item.Key.ToString());
                        }

                    }
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                return false;
            }


        }
        public async Task<bool> DeleteOneMemberGroupAsync(GroupMember member)
        {
            try
            {
                FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("GroupMember");
                JObject jsonResponse = firebaseResponse.ResultAs<JObject>();


                if (jsonResponse != null)
                {
                    // lọc bài viết mới
                    foreach (var item in jsonResponse)
                    {
                        var value = item.Value!.ToString();
                        //path Object
                        var mess = JsonConvert.DeserializeObject<GroupMember>(value);
                        if (mess.GroupId == member.GroupId && mess.UserId == member.UserId)
                        {
                            await _firebase._client.DeleteAsync("GroupMember/" + item.Key.ToString());
                            break;
                        }

                    }
                    return true;
                }
                return false;
            }
            catch (Exception ex)
            {
                return false;
            }


        }
        public async Task<bool> DeleteOneDetailAsync(DetailsChat dt)
        {
            try
            {
               
                SetResponse setResponse = await _firebase._client.SetAsync("DetailsChat/" + dt.DetailId, dt);
                return true;
            }
            catch (Exception ex)
            {
                return false;
            }


        }
    }
}
