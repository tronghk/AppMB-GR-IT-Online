using AppGrIT.Entity;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IUsers
    {
        public Task<ResponseModel> SignUpAsync(SignUpModel model);
        public Task<ResponseModel> SignInAsync(SignInModel model);
        public Task<ResponseModel> SignInGoogleAsync(string idToken);
        public Task<ResponseModel> SignUpGoogleAsync(string link);
        public Task<string> GetEmailModelFromLink(string link); 
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

        public Task<List<UserModel>> FindUserByLastName(string LastName);
        public Task<List<UserModel>> FindUserByAddress(string Address);
        public Task<List<UserModel>> FindUserByAge(int age);
        public Task<List<UserModel>> FindUserByLastName_Address_Age(string input);

        public Task<ResponseModel> SignUpAdminAsync(SignUpModel model);
        public Task<int> GetSumUser();
        public Task<List<UserModel>> GetUserLocked();
        public Task<ResponseModel> Unlock(string userId);
    }
}
