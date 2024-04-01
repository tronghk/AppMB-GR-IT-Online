namespace AppGrIT.Services
{
    public interface IUnUser
    {
        public Task<int> CountUnUserInAUser(string userId);
    }
}
