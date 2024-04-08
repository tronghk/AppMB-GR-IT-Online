using AppGrIT.Entity;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IUsers
    {
        public Task<ResponseModel> SignUpAsync(SignUpModel model);
        public Task<ResponseModel> SignInAsync(SignInModel model);
        public Task<ResponseModel> CreateAccount(AccountIdentity account);
        public Task<ResponseModel> UpdateRefeshTokenAccountAsync(string email, string refreshToken, DateTime expiryTime);

        public Task <AccountIdentity> GetUserAsync(string email);
        public Task<ResponseModel> ForgotPassword(string email);
        public Task<ResponseModel> VertificationEmail(SignInModel model);

        public Task<ResponseModel> ChangePasswordModel(ChangePasswordModel model);
        public Task<UserInforModel> GetInforUser(string email);

        public Task<ResponseModel> EditUserInfors(UserInforModel model);

        public Task<AccountIdentity> GetUserToUserId(string userId);

        public Task<UserModel> GetInfoUser(string userId);

        public Task<List<UserInforModel>> FindUserByLastName(string LastName);
        public Task<List<UserInforModel>> FindUserByAddress(string Address);

    }
}
