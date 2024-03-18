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

    }
}
