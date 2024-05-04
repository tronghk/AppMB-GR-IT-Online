using AppChat.Data;
using AppChat.Entity;
using AppChat.Models;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using System;
using System.Reflection;

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
                UserOrtherId = model.UserOrtherId,

            };
            
            var result = await _messageDao.AddChatSegMentAsync(chat);
            if (result.Status.Equals(StatusResponse.STATUS_SUCCESS))
            {
                model.MessId = result.Message!;
                return model;
            }
            return null!;


        }
        public async Task<ChatModel> GetChatToId(string chatId)
        {

            var result = await _messageDao.GetChatToId(chatId);

           if(result != null)
            {
                var chat = new ChatModel
                {
                    MessId = result.MessId,
                    UserId = result.UserId,
                    UserOrtherId = result.UserOrtherId,
                };
                return chat;
            }
           return null!;


        }
        public async Task<ChatModel> GetChatToUserId(string chatId)
        {

            var result = await _messageDao.GetChatToUserId(chatId);

            if (result != null)
            {
                var chat = new ChatModel
                {
                    MessId = result.MessId,
                    UserId = result.UserId,
                    UserOrtherId = result.UserOrtherId,
                };
                return chat;
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

           
                var mess = new DetailsChat
                {
                    ChatId = model.ChatId,
                    UserId = model.UserId,
                    DetailId = model.DetailId!,
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

        public async Task<GroupMemberModel> GetUserMemberToId(string groupId, string userId)
        {
            var result = await _messageDao.GetUserMemberToId(groupId, userId);

           if(result != null)
            {
                var model = new GroupMemberModel
                {
                    GroupId = groupId,
                    UserId = userId,
                    Role = result.Role
                };

                return model;
            }
            return null!;
        }

        public async Task<GroupMemberModel> UpdateGroupMember(GroupMemberModel model, string roleName)
        {
            var member = new GroupMember
            {
                GroupId = model.GroupId,
                UserId = model.UserId,
                Role = roleName
            };
            var result = await _messageDao.UpdateGroupMemberRole(member);
            if(result != null)
            {
                model.Role = roleName;
                return model;
            }
            return null!;
        }

        public async Task<ResponseModel> UpdateAdminGroupMember(GroupMemberModel userAdmin, GroupMemberModel userMember)
        {
            var member = new GroupMember
            {
                GroupId = userAdmin.GroupId,
                UserId = userAdmin.UserId,
                Role = SynthesizeRoles.GR_MEMBER
            };
            var member1 = new GroupMember
            {
                GroupId = userMember.GroupId,
                UserId = userMember.UserId,
                Role = userAdmin.Role
            };

            var result = await _messageDao.UpdateGroupMemberRole(member);
            var result1 = await _messageDao.UpdateGroupMemberRole(member1);
            if(result1 != null && result != null) {

                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = MessageResponse.MESSAGE_UPDATE_SUCCESS
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_UPDATE_FAIL
            };
        }

        public async Task<bool> CheckChatEx(string userId, string userIdOrther)
        {
            return await _messageDao.CheckChatExist(userId, userIdOrther);
        }

        public async Task<GroupChatModel> GetGroupChatModel(string chatId)
        {
            return null;
        }

        public async Task<ChatModel> GetChatModel(string chatId)
        {
            var result = await _messageDao.GetChatToId(chatId);
           if(result != null)
            {
                var chat = new ChatModel
                {
                    MessId = result.MessId
               ,
                    UserId = result.UserId
               ,
                    UserOrtherId = result.UserOrtherId
                };
                return chat;
            }
            return null;

        }

        public async Task<DetailsChatModel> GetDetailsMessageToId(string details)
        {
            var reuslt = await _messageDao.GetDetailsMessageToId(details);
            if(reuslt != null)
            {
                var de = new DetailsChatModel
                {
                    ChatId = reuslt.ChatId
                    , UserId = reuslt.UserId
                    ,DetailId = reuslt.DetailId,
                    Time = reuslt.Time,
                    Content = reuslt.Content,
                    ImagePath = reuslt.ImagePath,
                    Status = reuslt.Status

                };
                return de;
            }
            return null;
        }

        public async Task<bool> CheckRoleUser(string userId, List<GroupMemberModel> list)
        {
            foreach (var member in list)
            {
                if (member.UserId == userId && member.Role == SynthesizeRoles.GR_MANAGER)
                    return true;
            }
            return false;
        }

        public async Task<List<UserModel>> GetListChat(string userId)
        {
         
           List<UserModel> listChat = await _messageDao.GetListChatId(userId);
            List<UserModel> listGr = await _messageDao.GetListGroupId(userId);
            foreach (var member in listChat)
            {
                listGr.Add(member);

            }
            return listGr;


        }

        public async Task<List<GroupMemberModel>> GetGroupMember(string groupId)
        {
            var result = await _messageDao.GetMemberGroupToId(groupId);
            List<GroupMemberModel> list = new List<GroupMemberModel>();
            foreach(var member in result)
            {
                var value = new GroupMemberModel
                {
                    UserId = member.UserId,
                    Role = member.Role,
                    GroupId = member.GroupId,
                };
                list.Add(value);
            }
            return list;
        }
    }
}
