using AppChat.Data;
using AppChat.Entity;
using AppChat.Models;
using AppGrIT.Helper;
using AppGrIT.Model;
using System;

namespace AppChat.Services.Imployment
{
    public class MessageServices : IMessages
    {
        private readonly MessageDAO _messageDao;

        public MessageServices(MessageDAO message)
        {
            _messageDao = message;
        }

        public async Task<bool> CheckChatEx(string chatId)
        {
            var chat = await _messageDao.CheckChatExist(chatId);
            if (!chat)
            {
                var gr = await _messageDao.CheckChatGrExist(chatId);
                if (!gr)
                {
                    return false;
                }
            }
            return true;
        }

        public async Task<bool> CheckUserEx(string userId)
        {
          return await _messageDao.CheckUserValid(userId);
           
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

        public async Task<GroupChatModel> CreateGroupModelAsync(GroupChatModel model)
        {
            var gr = new GroupChat
            {
                TimeCreate = model.TimeCreate,
                GroupName = model.GroupName,
                ImagePath = model.ImagePath,
            };
            var result = await _messageDao.AddGroupChatAsync(gr);

            if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
            {
                model.GroupId = result.Message!;
                return model;
            }
            return null!;
        }

        public async Task<List<GroupMemberModel>> CreateMemberGroupModelAsync(List<GroupMemberModel> list, string grId)
        {
           foreach (var member in list)
            {
                var mem = new GroupMember
                {
                    GroupId = grId,
                    UserId = member.UserId,
                    Role = member.Role,
                };
                var result = await _messageDao.AddMemberGroupChatAsync(mem);
                if(result.Status == StatusResponse.STATUS_ERROR)
                {
                    return null!;
                }
                else
                {
                    member.GroupId = grId;
                }
            }
            return list;
        }

        public async Task<DetailsChatModel> CreateMessageModelAsync(DetailsChatModel model)
        {
            var mess = new DetailsChat
            {
                ChatId = model.ChatId,
                UserId = model.UserId,
                Time = model.Time,
                Content = model.Content,
                ImagePath = model.ImagePath,
            };
            var result = await _messageDao.AddMessageAsync(mess);
           
            if (result.Status.Equals(StatusResponse.STATUS_SUCCESS))
            {
                model.DetailId = result.Message!;
                return model;
            }
            return null!;

        }

        public async Task<ResponseModel> DeleteChatModelAsync(ChatModel model)
        {
            var reDetail = await _messageDao.DeleteDetailChatSegMentAsync(model.MessId);
            var result = await _messageDao.DeleteChatSegMentAsync(model.MessId);
            
                if(result == MessageResponse.MESSAGE_DELETE_SUCCESS && reDetail)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = result
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = result
        };
    }

        public async Task<ResponseModel> DeleteMessageModelAsync(DetailsChatModel model)
        {

            var isChat = await CheckChatEx(model.ChatId);
            if(isChat)
            {
                var mess = new DetailsChat
                {
                    ChatId = model.ChatId,
                    UserId = model.UserId,
                    Time = model.Time,
                    Content = model.Content,
                    ImagePath = model.ImagePath,
                    Status = "recall"
                };
                var reDetail = await _messageDao.DeleteOneDetailAsync(mess);
                if (reDetail)
                {
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_SUCCESS,
                        Message = MessageResponse.MESSAGE_DELETE_SUCCESS
                    };
                }
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_DELETE_FAIL
                };

            }
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_NOTFOUND,
                    Message = MessageResponse.MESSAGE_NOTFOUND
                };

        }
        public bool CheckRoleMember(string memberId, List<GroupMemberModel> list, string role)
        {
            foreach (var member in list)
            {
                if(member.UserId == memberId)
                {
                    if(member.Role == role)
                        return true;
                }
            }
            return false;
        }

        public async Task<ResponseModel> DeleteGroupChatAsync(GroupChatModel model)
        {
            var reDetail = await _messageDao.DeleteDetailChatSegMentAsync(model.GroupId);
            var reMember = await _messageDao.DeleteMemberGroupAsync(model.GroupId);
            var result = await _messageDao.DeleteGroupChatAsync(model.GroupId);

            if (result == MessageResponse.MESSAGE_DELETE_SUCCESS && reDetail && reMember)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = result
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = result
            };
        }

        public async Task<GroupChatModel> UpdateGroupModelAsync(GroupChatModel model)
        {
            var gr = new GroupChat
            {
                GroupId = model.GroupId,
                TimeCreate = model.TimeCreate,
                GroupName = model.GroupName,
                ImagePath = model.ImagePath,
            };
            var result = await _messageDao.UpdateGroupChatAsync(gr);

            if (result.Status!.Equals(StatusResponse.STATUS_SUCCESS))
            {
                model.GroupId = result.Message!;
                return model;
            }
            return null!;
        }

        public async Task<ResponseModel> DeleteOneMemberGroupAsync(GroupMemberModel model)
        {
            var member = new GroupMember
            {
                GroupId = model.GroupId,
                UserId = model.UserId,

            };
            var result = await _messageDao.DeleteOneMemberGroupAsync(member);
            if (result)
            {
                return new ResponseModel { Status = StatusResponse.STATUS_SUCCESS, Message = MessageResponse.MESSAGE_DELETE_SUCCESS };
            }
            return new ResponseModel { Status = StatusResponse.STATUS_ERROR, Message = MessageResponse.MESSAGE_DELETE_FAIL };

        }
    }
}
