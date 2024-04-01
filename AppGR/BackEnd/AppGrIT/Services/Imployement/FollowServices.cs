using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services.Imployement
{
    public class FollowServices : IFollows
    {
        private readonly FollowDAO _userFollowDAO;
        public FollowServices(FollowDAO us) {
        
            _userFollowDAO = us;
        }

        public async Task<UserFollowsModel> CreateUserFollow(UserFollowsModel model)
        {
            var userfl = new UserFollows
            {
                UserFollowId = model.UserFollowId,
                UserId = model.UserId,
            };
            var result = await _userFollowDAO.CreateFollowAsync(userfl);
            if (result.Equals(StatusResponse.STATUS_SUCCESS))
                return model;
            return null;
        }

        public async Task<ResponseModel> DeleteUserFollow(UserFollowsModel model)
        {
            var userfl = new UserFollows
            {
                UserFollowId = model.UserFollowId,
                UserId = model.UserId,
            };
            var result = await _userFollowDAO.DeleteUserFollow(userfl);
            if (result.Equals(StatusResponse.STATUS_SUCCESS))
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = MessageResponse.MESSAGE_CREATE_SUCCESS
                };
              
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            };
        }

        public async Task<List<UserFollowsModel>> GetListUserFollow(string userId)
        {
            List<UserFollowsModel> result = new List<UserFollowsModel>();
            var list = await _userFollowDAO.GetUserFollow(userId);
            foreach (var userFollow in list)
            {
                var us = new UserFollowsModel
                {
                    UserId = userFollow.UserId,
                    UserFollowId = userFollow.UserId,
                };
                result.Add(us);
            }
            return result;
        }

        public async Task<UserFollowsModel> GetUserFollow(string userId, string userFl)
        {
            var userFollow = await _userFollowDAO.GetUserFollow(userId,userFl);

                var us = new UserFollowsModel
                {
                    UserId = userFollow.UserId,
                    UserFollowId = userFollow.UserId,
                };
               
            
            return us;
        }
    }
}
