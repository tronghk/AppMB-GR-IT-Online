
using AppGrIT.Data;
using AppGrIT.Services.AppGrIT.Services;

namespace AppGrIT.Services.Imployement
{
    public class UserFollowsServices : IUserFollows
    {
        private UserFollowsDAO _followersDAO;

        public UserFollowsServices(UserFollowsDAO followers)
        {
            _followersDAO = followers;

        }
        public async Task<int> CountFollowersInAUser(string userId)
        {
            var countFollower = await _followersDAO.CountFollowers(userId);
            return countFollower;
        }
        public async Task<int> CountUserFollowersInAUser(string userId)
        {
            var countFollower = await _followersDAO.CountUserFollowers(userId);
            return countFollower;
        }
    }
}
