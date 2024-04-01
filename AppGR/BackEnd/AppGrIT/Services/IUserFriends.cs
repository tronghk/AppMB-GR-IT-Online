using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IUserFriends
    {
        public Task<int> CountFriendsInAUser(string userId);
        public Task<List<UserFriendsModel>> GetListUserFriends(string userId);
    }
}
