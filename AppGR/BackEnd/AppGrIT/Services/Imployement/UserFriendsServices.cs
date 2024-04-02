using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Models;
using AppGrIT.Services.AppGrIT.Services;
using Firebase.Auth;

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
                };
                result.Add(us);
            }
            return result;
        }

        public async Task<UserFriendsModel> CreateUserFriend(UserFriendsModel model)
        {
            
            var userfr = new UserFriends
            {
                UserFriendId = model.UserFriendId,
                UserId = model.UserId,           
            };

            var result = await _userFriendsDAO.CreateFriendAsync(userfr);
            if (result.Equals(StatusResponse.STATUS_SUCCESS))
                return model;

            return null;
        }



        public async Task<UserFriendsModel> GetUserFriend(string userId, string userFr)
        {
            var userFriend = await _userFriendsDAO.GetUserFriend(userId, userFr);

            if (userFriend != null)
            {
                var us = new UserFriendsModel
                {
                    UserId = userFriend.UserId,
                    UserFriendId = userFriend.UserFriendId,                  
                };

                return us;
            }
            else
            {
                return null;
            }

        }
    }
}
