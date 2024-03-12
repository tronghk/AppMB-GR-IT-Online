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
        private const string API_key = "AIzaSyBaiAShjOJltsKlLltkrIJ4z_ZETAprT-A";

        public async Task<ResponseModel> SignInAsync(SignInModel model)
        {
           try
            {
                FirebaseAuthProvider firebaseAuthProvider = new FirebaseAuthProvider(new FirebaseConfig(API_key));
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


            var user = new AccountIdentity
            {
                Email = model.Email,    
                FirstName = model.FirstName,
                LastName = model.LastName,
                Birthday = model.Birthday,
            };

            //tạo tk
            FirebaseAuthProvider firebaseAuthProvider = new FirebaseAuthProvider(new FirebaseConfig(API_key));
            FirebaseAuthLink link = await firebaseAuthProvider.CreateUserWithEmailAndPasswordAsync(model.Email, model.Password);

            //xét quyền mặc định


            // xác thực email :

            return new ResponseModel
            {
                Status = "Ok",
                Message = "comfirm"
            };

        }
    }
}
