using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using Firebase.Auth;
using FirebaseAdmin.Auth;
using FirebaseAdmin.Messaging;
using FireSharp.Extensions;
using Google.Api.Gax.Rest;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json;
using System.Data;
using System.IdentityModel.Tokens.Jwt;
using System.Reflection;
using System.Text;

namespace AppGrIT.Services.Imployement
{
    public class UserServices : IUsers
    {
       
      
        private readonly UsersDAO _userDao;
        private readonly PostsDAO _postsDao;
        private readonly IRoles _roleManager;
        private readonly IConfiguration _configuration;
        private readonly FirebaseAuthProvider _firebaseAuth;
        private readonly ImagesDAO _imagesDao;


        public UserServices(IConfiguration configuration, UsersDAO user, IRoles role, PostsDAO postsDao, ImagesDAO imagesDao)
        {
            _configuration = configuration;
            _userDao = user;
            _roleManager = role;
            _firebaseAuth = new FirebaseAuthProvider(new FirebaseConfig(_configuration["Firebase:API_Key"]));
            _postsDao = postsDao;
            _imagesDao = imagesDao;
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
                    Status = StatusResponse.STATUS_SUCCESS,
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
        public async Task<ResponseModel> SignUpGoogleAsync(string link)
        {
            var user = await _firebaseAuth.GetUserAsync(link);
            
            try
            {
               
                var us = new AccountIdentity
                {
                    Email = user.Email

                };

               

                var result = await CreateAccount(us);
                UserInforModel userInfos = new UserInforModel
                {
                    Firstname = user.FirstName,
                    LastName = user.LastName,
                };
                await CreateUserInfors(userInfos, user.Email);
                //xét quyền mặc định
                await SetRoleDefault(user.Email, SynthesizeRoles.CUSTOMER);

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
                LastName = model.LastName,
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
                return new ResponseModel { Status = StatusResponse.STATUS_SUCCESS };

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
                    Status = StatusResponse.STATUS_SUCCESS,

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
                    Status = StatusResponse.STATUS_SUCCESS,

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
                LastName = userInfor.LastName,
                Gender = userInfor.Gender,
                Birthday = userInfor.Birthday,
                Address = userInfor.Address,
                UserId = userInfor.UserId,
                Phone = userInfor.Phone,

            };
            return us;

        }

        public async Task<ResponseModel> EditUserInfors(UserInforModel model)
        {
            var us = new UserInfors
            {
                Firstname = model.Firstname,
                LastName = model.LastName,
                Gender = model.Gender!,
                Birthday = model.Birthday,
                Address = model.Address!,
                UserId = model.UserId,
                Phone = model.Phone!,
            };
            var result = await _userDao.EditUserInfors(us);
            return result;
        }

        public async Task<AccountIdentity> GetUserToUserId(string userId)
        {
           return await _userDao.GetUserToUserIdAsync(userId);
        }

        public async Task<UserModel> GetInfoUser(string userId)
        {
            var post = await GetPostNewInfoUser(userId);
            var image = post.imagePost;
            var pathImage = image[0].ImagePath;
            var userInfo = await _userDao.GetUserInforAsync(userId);
            var user = new UserModel
            {
                UserId = userId,
                ImagePath = pathImage,
                UserName = userInfo.LastName + userInfo.Firstname
            };
            return user;
        }
        private async Task<PostModel> GetPostNewInfoUser(string userId)
        {
            var list = await _postsDao.GetUserInstead(userId);

            var idNew = list[0];
            foreach (var post in list)
            {
                if (post.PostTime > idNew.PostTime)
                {
                    idNew = post; break;
                }
            }
            var listImage = await GetImagePostToId(idNew.PostId);

            return new PostModel
            {
                PostId = idNew.PostId,
                Content = idNew.Content,
                PostTime = idNew.PostTime,
                PostType = idNew.PostType,
                UserId = idNew.UserId!,
                imagePost = listImage
            };

        }
        public async Task<List<ImagePostModel>> GetImagePostToId(string postId)
        {
            var list = await _imagesDao.GetImagePostToId(postId);
            var result = new List<ImagePostModel>();
            foreach (var imagePost in list)
            {
                var im = new ImagePostModel
                {
                    ImagePath = imagePost.ImagePath,
                    ImageContent = imagePost.ImageContent,
                    ImageId = imagePost.PostImageId,

                };
                result.Add(im);
            }
            return result;
        }

        public async Task<ResponseModel> SignInGoogleAsync(string idToken )
        {
            try
            {
                FirebaseAuthLink link = await _firebaseAuth.SignInWithGoogleIdTokenAsync(idToken);

                if (link != null)
                {
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_SUCCESS,
                        Message = link.FirebaseToken
                    };
                }
            }
            catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_LOGIN_FAIL
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_LOGIN_FAIL
            };





        }

        public async Task<string> GetEmailModelFromLink(string link)
        {
            var user = await _firebaseAuth.GetUserAsync(link);
            var email = user.Email;
            if(user!= null)
            {
                return email;
            }
            return null;
        }
    }
}
