
using AppGrIT.Data;
using AppGrIT.Services.AppGrIT.Services;

namespace AppGrIT.Services.Imployement
{
    public class FollowersServices : IFollowers
    {
        private FollowersDAO _followersDAO;

        public FollowersServices(FollowersDAO followers)
        {
            _followersDAO = followers;

        }
        public async Task<int> CountFollowersInAUser(string userId)
        {
            var countFollower = await _followersDAO.CountFollowers(userId);
            return countFollower;
        }
    }
}
