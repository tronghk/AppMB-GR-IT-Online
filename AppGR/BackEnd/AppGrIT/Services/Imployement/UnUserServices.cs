using AppGrIT.Data;
using AppGrIT.Services.AppGrIT.Services;
namespace AppGrIT.Services.Imployement
{
    public class UnUserServices : IUnUser
    {
        private UnUserDAO _unUserDAO;

        public UnUserServices(UnUserDAO unUser)
        {
            _unUserDAO = unUser;

        }
        public async Task<int> CountUnUserInAUser(string userId)
        {
            var countunUser = await _unUserDAO.CountUnUser(userId);
            return countunUser;
        }
    }
}
