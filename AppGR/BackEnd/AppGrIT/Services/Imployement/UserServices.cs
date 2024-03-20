using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using Firebase.Auth;
using Microsoft.AspNetCore.Identity;
using System.Data;

namespace AppGrIT.Services.Imployement
{
    public class UserServices : IUsers
    {
       
      
        private readonly UsersDAO _userDao;
        private readonly IRoles _roleManager;
        private readonly IConfiguration _configuration;


        public UserServices(IConfiguration configuration, UsersDAO user, IRoles role)
        {
            _configuration = configuration;
            _userDao = user;
            _roleManager = role;
        }

        public async Task<ResponseModel> CreateAccount(AccountIdentity account)
        {
            var result = await _userDao.AddUserAsync(account);
            return result;
        }

        public async Task<ResponseModel> SignInAsync(SignInModel model)
        {
           try
            {
                FirebaseAuthProvider firebaseAuthProvider = new FirebaseAuthProvider(new FirebaseConfig(_configuration["Firebase:API_Key"]));
                FirebaseAuthLink link = await firebaseAuthProvider.SignInWithEmailAndPasswordAsync(model.Email, model.Password);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_OK,
                    Message = link.FirebaseToken
                };
            }
            catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR
                  ,
                    Message = "login fail"
                };
            }
           

        }

        public async Task<ResponseModel> SignUpAsync(SignUpModel model)
        {
            try
            {
                FirebaseAuthProvider firebaseAuthProvider = new FirebaseAuthProvider(new FirebaseConfig(_configuration["Firebase:API_Key"]));
                var us = new AccountIdentity
                {
                    Email = model.Email,
                   
                };

                //tạo tk
                FirebaseAuthLink link = await firebaseAuthProvider.CreateUserWithEmailAndPasswordAsync(model.Email, model.Password);
                var result = await CreateAccount(us);
                //xét quyền mặc định
                await SetRoleDefault(model.Email,SynthesizeRoles.CUSTOMER);

                return result;
            }
            catch
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Email exist"
                };
            }

        }
        public async Task <ResponseModel> SetRoleDefault(string Email, string roleName)
        {
            Roles role = await _roleManager.GetRoleAsync(roleName);
            if (role == null) {
                RolesModel model = new RolesModel
                {
                    RoleName = roleName,
                    Despripsion = "Khách hàng"
                };
                await _roleManager.AddRoleAsync(model);
            }

            UserRoleModel us = new UserRoleModel
            {
                Email = Email,
                RoleName = roleName,
            };
            var reuslt = await _roleManager.AddUserRolesAsync(us);
            return reuslt;
        }

        public async Task<ResponseModel> UpdateRefeshTokenAccountAsync(string email, string refreshToken, DateTime expiryTime)
        {
            var result = await _userDao.UpdateRefreshToken(email, refreshToken, expiryTime);
            return result;
        }

        public async Task<AccountIdentity> GetUserAsync(string email)
        {
           return await _userDao.GetUserAsync(email);
        }
    }
}
