using AppChat.Entity;
using AppChat.Models;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppChat.Services
{
    public interface IMessages
    {
        public Task<ChatModel>  CreateChatModelAsync(ChatModel model);
        public Task<ChatModel> GetChatToId(string chatId);
        public Task<ChatModel> GetChatToUserId(string chatId);
        public Task<GroupMemberModel> GetUserMemberToId(string groupId, string userId);
        public Task<GroupMemberModel> UpdateGroupMember(GroupMemberModel model, string roleName);
        public Task<ResponseModel> UpdateAdminGroupMember(GroupMemberModel admin, GroupMemberModel member);
        public Task<ResponseModel> DeleteChatModelAsync(ChatModel model);

        public Task<ResponseModel> DeleteMessageModelAsync(DetailsChatModel model);

        public Task<ResponseModel> DeleteGroupChatAsync(GroupChatModel model);

        public Task<ResponseModel> DeleteOneMemberGroupAsync(GroupMemberModel model);

        public Task<DetailsChatModel> CreateMessageModelAsync(DetailsChatModel model);

        public Task<GroupChatModel> CreateGroupModelAsync(GroupChatModel model);
        public Task<GroupChatModel> UpdateGroupModelAsync(GroupChatModel model);

        public Task<List<GroupMemberModel>> CreateMemberGroupModelAsync(List<GroupMemberModel> list, string grId);

        public Task<bool> CheckUserEx(string userId);
        public Task<bool> CheckChatEx(string chatId);
        public Task<bool> CheckChatEx(string userId,string userIdOrther);
        public bool CheckRoleMember(string memberId, List<GroupMemberModel> list, string role);
        public Task<GroupChatModel> GetGroupChatModel(string chatId);
        public Task<ChatModel> GetChatModel(string chatId);
        public Task<DetailsChatModel> GetDetailsMessageToId(string details);
        public Task<bool> CheckRoleUser(string userId, List<GroupMemberModel> list);
        public Task<List<UserModel>> GetListChat(string userId);
        public Task<List<GroupMemberModel>> GetGroupMember(string groupId);
    }
}
