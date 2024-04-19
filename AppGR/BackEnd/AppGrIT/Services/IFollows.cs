using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IFollows
    {
        public Task<UserFollowsModel> CreateUserFollow(UserFollowsModel model);
        public Task<ResponseModel> DeleteUserFollow(UserFollowsModel model);
        public Task<List<UserFollowsModel>> GetListUserFollow(string userId);
       
        public Task<UserFollowsModel> GetUserFollow(string userId, string userFl);

    }
}
