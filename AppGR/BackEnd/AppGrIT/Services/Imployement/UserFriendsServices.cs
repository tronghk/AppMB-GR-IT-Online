using AppGrIT.Data;
using AppGrIT.Services.AppGrIT.Services;

namespace AppGrIT.Services.Imployement
{
    public class UserFriendsServices : IUserFriends
    {
        private UserFriendsDAO _userFriendsDAO;

        public UserFriendsServices(UserFriendsDAO useFriend)
        {
            _userFriendsDAO = useFriend;

        }
        public async Task<int> CountFriendsInAUser(string userId)
        {
            var countFriend = await _userFriendsDAO.CountUserFriends(userId);
            return countFriend;
        }
    }
}
