using AppGrIT.Data;
using AppGrIT.Models;
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
        public async Task<List<UnUserModel>> GetListUnUser(string userId)
        {
            List<UnUserModel> result = new List<UnUserModel>();
            var list = await _unUserDAO.GetListUnUser(userId);
            foreach (var unUser in list)
            {
                var us = new UnUserModel
                {
                    UserId = unUser.UserId,
                    UnUserId = unUser.UnUserId,            
                };
                result.Add(us);
            }
            return result;
        }
    }
}
