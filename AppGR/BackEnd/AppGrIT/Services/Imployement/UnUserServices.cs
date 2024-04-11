using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Models;
using AppGrIT.Services.AppGrIT.Services;
using Firebase.Auth;
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

        public async Task<UnUserModel> GetUnUser(string userId, string Unuser)
        {
            var unUser = await _unUserDAO.GetUnUser(userId, Unuser);

            if (unUser != null)
            {
                var us = new UnUserModel
                {
                    UserId = unUser.UserId,
                    UnUserId = unUser.UnUserId,
                };

                return us;
            }
            else
            {
                return null;
            }
        }
        public async Task<UnUserModel> CreateUnUser(UnUserModel model)
        {
            var unUser = new UnUser
            {
                UnUserId = model.UnUserId,
                UserId = model.UserId,
            };

            var result = await _unUserDAO.CreateUnUserAsync(unUser);
            if (result.Equals(StatusResponse.STATUS_SUCCESS))
                return model;

            return null;
        }
    }
}
