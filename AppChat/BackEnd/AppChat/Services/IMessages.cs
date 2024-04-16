using AppChat.Entity;
using AppChat.Models;
using AppGrIT.Model;

namespace AppChat.Services
{
    public interface IMessages
    {
        public Task<ChatModel>  CreateChatModelAsync(ChatModel model);
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
        public bool CheckRoleMember(string memberId, List<GroupMemberModel> list, string role);
    }
}
