namespace AppGrIT.Services
{
    namespace AppGrIT.Services
    {
        public interface IUserFollows
        {
            public Task<int> CountFollowersInAUser(string userId);

            public Task<int> CountUserFollowersInAUser(string userId);
        }
    }

}
