using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IUnUser
    {
        public Task<int> CountUnUserInAUser(string userId);
        public Task<List<UnUserModel>> GetListUnUser(string userId);
    }
}
