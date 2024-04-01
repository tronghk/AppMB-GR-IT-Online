namespace AppGrIT.Services
{
    public interface IUserFriends
    {
        public Task<int> CountFriendsInAUser(string userId);
    }
}
