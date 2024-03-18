using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Model;
using AppGrIT.Models;
using Firebase.Auth;
using Microsoft.AspNetCore.Identity;
using System.Data;

namespace AppGrIT.Services.Imployement
{
    public class UserServices : IUsers
    {
       
      
        private readonly UserDAO _userManger;
        private readonly IConfiguration _configuration;

        public UserServices(IConfiguration configuration, UserDAO user)
        {
            _configuration = configuration;
            _userManger = user;
        }

        public async Task<ResponseModel> CreateAccount(AccountIdentity account)
        {
            var result = await _userManger.AddUserFirebase(account);
            return result;
        }

        public async Task<ResponseModel> SignInAsync(SignInModel model)
        {
           try
            {
                FirebaseAuthProvider firebaseAuthProvider = new FirebaseAuthProvider(new FirebaseConfig(_configuration["Firebase:API_Key"]));
                FirebaseAuthLink link = await firebaseAuthProvider.SignInWithEmailAndPasswordAsync(model.Email, model.Password);
            }catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status = "Fail"
                  ,
                    Message = "login fail"
                };
            }
            return new ResponseModel
            {
                Status = "Ok",
                Message = "appect login"
            };

        }

        public async Task<ResponseModel> SignUpAsync(SignUpModel model)
        {
            //check mail

            // check duplicate

            var user = new AccountIdentity
            {
                Email = model.Email,    
                FirstName = model.FirstName,
                LastName = model.LastName,
                Birthday = model.Birthday,
            };

            //tạo tk
            FirebaseAuthProvider firebaseAuthProvider = new FirebaseAuthProvider(new FirebaseConfig(_configuration["Firebase:API_Key"]));
            FirebaseAuthLink link = await firebaseAuthProvider.CreateUserWithEmailAndPasswordAsync(model.Email, model.Password);
            var result = await CreateAccount(user);
            //xét quyền mặc định


            // xác thực email :

            return result;

        }
        
        
    }
}
