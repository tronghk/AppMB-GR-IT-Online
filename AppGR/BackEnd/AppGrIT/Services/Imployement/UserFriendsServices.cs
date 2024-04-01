using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Models;
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
        public async Task<List<UserFriendsModel>> GetListUserFriends(string userId)
        {
            List<UserFriendsModel> result = new List<UserFriendsModel>();
            var list = await _userFriendsDAO.GetListUserFriends(userId);
            foreach (var userFriends in list)
            {
                var us = new UserFriendsModel
                {
                    UserId = userFriends.UserId,
                    UserFriendId = userFriends.UserFriendId,
                    FriendShipTime = userFriends.FriendShipTime,
                };
                result.Add(us);
            }
            return result;
        }

    }
}
