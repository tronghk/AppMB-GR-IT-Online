using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using Firebase.Auth;
using FirebaseAdmin.Messaging;
using FireSharp.Extensions;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using System.Data;
using System.IdentityModel.Tokens.Jwt;
using System.Text;

namespace AppGrIT.Services.Imployement
{
    public class UserServices : IUsers
    {
       
      
        private readonly UsersDAO _userDao;
        private readonly IRoles _roleManager;
        private readonly IConfiguration _configuration;
        private readonly FirebaseAuthProvider _firebaseAuth;


        public UserServices(IConfiguration configuration, UsersDAO user, IRoles role)
        {
            _configuration = configuration;
            _userDao = user;
            _roleManager = role;
            _firebaseAuth = new FirebaseAuthProvider(new FirebaseConfig(_configuration["Firebase:API_Key"]));
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
               
                FirebaseAuthLink link = await _firebaseAuth.SignInWithEmailAndPasswordAsync(model.Email, model.Password);
                var user = await GetUserAsync(model.Email);
                if(user.Locked)
                {
                   DateTime date = user.TimeLocked;
                    if(date >= DateTime.UtcNow) {

                        return new ResponseModel
                        {
                            Status = StatusResponse.STATUS_ERROR,
                            Message = "Account is locked"
                        };
                    }
                    else
                    {
                            await _userDao.UnlockUserAsync(user);
                    }
                }
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_OK,
                    Message = link.FirebaseToken
                };
            }
            catch (Exception ex)
            {
                var user = await GetUserAsync(model.Email);
                if(user != null)
                {
                    if(user.countLocked == 2)
                    {
                        DateTime date = DateTime.UtcNow.AddMinutes(30);
                       _userDao.LockUser(user,date);
                    }
                    else
                    {
                        _userDao.UpLock(user);
                    }
                }
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
               
                var us = new AccountIdentity
                {
                    Email = model.Email,
                   
                };

                //tạo tk
                FirebaseAuthLink link = await _firebaseAuth.CreateUserWithEmailAndPasswordAsync(model.Email, model.Password);
              
                var result = await CreateAccount(us);
                UserInforModel userInfos = new UserInforModel
                {
                    Firstname = model.FirstName,
                    LastName = model.LastName,
                    Birthday = model.Birthday

                };
                await CreateUserInfors(userInfos, model.Email);
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
        private async Task<ResponseModel> CreateUserInfors(UserInforModel model,string email)
        {
            var user = await GetUserAsync(email);
            UserInfors us = new UserInfors
            {
                Firstname = model.Firstname,
                Lastname = model.LastName,
                Birthday = model.Birthday,
                Gender = model.Gender,
                Address = model.Address,
                UserId = user.UserId,
                Phone = model.Phone,

            };
             await _userDao.AddUserInforsAsync(us);
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_SUCCESS,

            };
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

        public async Task<ResponseModel> ForgotPassword(string email)
        {
            var user = await _userDao.GetUserAsync(email);
            if(user!= null)
            {
                var result = _firebaseAuth.SendPasswordResetEmailAsync(email);
                if (result == null)
                {
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_ERROR,
                        Message = "Can not reset password"
                    };
                  
                }
                return new ResponseModel { Status = StatusResponse.STATUS_OK };

            }

            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_NOTFOUND
            };


           
        }

        public async Task<ResponseModel> VertificationEmail(SignInModel model)
        {
            

            try
            {
                var user = await _firebaseAuth.SignInWithEmailAndPasswordAsync(model.Email, model.Password);
                var isVertification = await _firebaseAuth.GetUserAsync(user);
                if (isVertification.IsEmailVerified)
                {
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_ERROR,
                        Message = "Email verified"
                    };

                }
                var result = _firebaseAuth.SendEmailVerificationAsync(user.FirebaseToken);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_OK,

                };
            }
            catch
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_LOGIN_FAIL
                };
            }
        }


        public async Task<ResponseModel> ChangePasswordModel(ChangePasswordModel model)
        {
            if (model.NewPassword.Equals(model.OldPassword))
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_NOTDUPLICATE
                };
            }
            try
            {
                var user = await _firebaseAuth.SignInWithEmailAndPasswordAsync(model.Email, model.OldPassword);
                
               
                var changepassword = await _firebaseAuth.ChangeUserPassword(user.FirebaseToken, model.NewPassword);
               
               
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_OK,

                };
            }
            catch
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_LOGIN_FAIL
                };
            }
            
        }

        public async Task<UserInforModel> GetInforUser(string email)
        {
            var user = await GetUserAsync(email);
            var UserId = user.UserId;
            var userInfor = await _userDao.GetUserInforAsync(UserId);
            UserInforModel us = new UserInforModel
            {
                Firstname = userInfor.Firstname,
                LastName = userInfor.Lastname,
                Gender = userInfor.Gender,
                Birthday = userInfor.Birthday,
                Address = userInfor.Address,
                UserId = userInfor.UserId,
                Phone = userInfor.Phone,

            };
            return us;

        }

    }
}
