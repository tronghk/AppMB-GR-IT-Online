namespace AppGrIT.Services
{
    namespace AppGrIT.Services
    {
        public interface IFollowers
        {
            public Task<int> CountFollowersInAUser(string userId);
        }
    }

}
