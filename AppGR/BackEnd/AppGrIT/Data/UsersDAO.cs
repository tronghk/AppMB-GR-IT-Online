using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using Firebase.Auth;
using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Globalization;
using System.Security.Principal;
using System.Text.Json.Nodes;
using static Google.Apis.Requests.BatchRequest;

namespace AppGrIT.Data
{
    public class UsersDAO
    {
        private readonly ConnectFirebase _firebase;
        public UsersDAO(ConnectFirebase connectFirebase) {
            _firebase = connectFirebase;
        }
       
        public async Task<ResponseModel> AddUserAsync(AccountIdentity account)
        {
            try
            {
                
                PushResponse response = await _firebase._client.PushAsync("Users/", account);

                account.UserId = response.Result.name;
                SetResponse setResponse = await _firebase._client.SetAsync("Users/" + account.UserId, account);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = "Register success"
                    
                };
            }catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status =  StatusResponse.STATUS_ERROR,
                    Message = ex.Message
                };
            }

           
        }
        public async Task<ResponseModel> UnLockUser(string userId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Users");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            try
            {
                AccountIdentity account;
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    account = JsonConvert.DeserializeObject<AccountIdentity>(value);
                    if (account.UserId.Equals(userId))
                    {
                        account.Locked = false;
                        SetResponse setResponse = await _firebase._client.SetAsync("Users/" + account.UserId, account);
                        return new ResponseModel
                        {
                            Status = StatusResponse.STATUS_SUCCESS,
                            Message = MessageResponse.MESSAGE_UPDATE_SUCCESS
                        };
                    }
                }
            }
            catch (Exception e) {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_UPDATE_FAIL
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_UPDATE_FAIL
            };


        }

        public async Task<ResponseModel> AddUserInforsAsync(UserInfors account)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("UserInfors/", account);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = "Register success"

                };
            }
            catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = ex.Message
                };
            }


        }
        public async void LockUser(AccountIdentity account, DateTime timeLock)
        {

            account.Locked = true;
            account.TimeLocked = timeLock;
            SetResponse setResponse = await _firebase._client.SetAsync("Users/" + account.UserId, account);
        }
        public async Task<string> UnlockUserAsync(AccountIdentity account)
        {

            account.Locked = false;
            account.countLocked = 0;
            SetResponse setResponse = await _firebase._client.SetAsync("Users/" + account.UserId, account);
            return "ok";
        }
        public async void UpLock(AccountIdentity account)
        {
            int count = account.countLocked;
            account.countLocked = count + 1;
            SetResponse setResponse = await _firebase._client.SetAsync("Users/" + account.UserId, account);
        }
        public async Task<List<AccountIdentity>> GetUsersAsync()
        {
           
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Users");
            dynamic data = JsonConvert.DeserializeObject<dynamic>(firebaseResponse.Body);
            var list = new List<AccountIdentity>();
            foreach(var item in data)
            {
                list.Add(JsonConvert.DeserializeObject<AccountIdentity> (((JProperty)item).Value.ToString()));
            }
            return list;
        }
        public async Task<AccountIdentity> GetUserAsync(string Email)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Users");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            AccountIdentity account = null!;
            bool result = false;
           if(jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    account = JsonConvert.DeserializeObject<AccountIdentity>(value);
                    if (account.Email.Equals(Email))
                    {
                        result = true;
                        break;
                    }
                }
                if (result)
                    return account;
            }
            return null!;

        }
        public async Task<AccountIdentity> GetUserToUserIdAsync(string userId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Users");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            AccountIdentity account;
            foreach (var item in jsonResponse)
            {
                var value = item.Value!.ToString();
                //path Object
                account = JsonConvert.DeserializeObject<AccountIdentity>(value);
                if (account.UserId.Equals(userId))
                {
                    return account;
                }
            }
            return null!;

        }

        public async Task<UserInfors> GetUserInforAsync(string UserId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserInfors");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            UserInfors userInfors = null!;
            foreach(var item in jsonResponse) {
                var value = item.Value!.ToString();
                //path Object
                userInfors = JsonConvert.DeserializeObject<UserInfors>(value);
                if(userInfors.UserId.Equals(UserId))
                {
                   return userInfors;
                }
            }
            return null!;

        }
        public async Task<int> GetSumUser()
        {
            int count = 0;
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Users");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            if(jsonResponse != null)
            {
                AccountIdentity userInfors = null!;
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    userInfors = JsonConvert.DeserializeObject<AccountIdentity>(value);
                    count = count + 1;
                }
            }
            return count;

        }
        public async Task<List<string>> GetUserLocked()
        {
            List<string> list = new List<string>(); 
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Users");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            if (jsonResponse != null)
            {
                AccountIdentity userInfors = null!;
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    userInfors = JsonConvert.DeserializeObject<AccountIdentity>(value);
                    if (userInfors.Locked)
                    {
                        list.Add(userInfors.UserId);
                    }
                }
            }
            return list;

        }

        public async Task<ResponseModel> UpdateRefreshToken(string email, string RefreshToken, DateTime exrityTime)
        {

            try
            {
                var user = await GetUserAsync(email);
                await _firebase._client.SetAsync("Users/" + user.UserId + "/RefreshToken", RefreshToken);
                await _firebase._client.SetAsync("Users/" + user.UserId + "/RefreshTokenExpiryTime", exrityTime);
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
                    Message = "Error RefreshToken"
                };
            }
            
        }
        public async Task<ResponseModel> EditUserInfors(UserInfors user)
        {
            try
            {
                FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserInfors");
                JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
                UserInfors userInfors = null!;
                var id = "";
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    userInfors = JsonConvert.DeserializeObject<UserInfors>(value);
                    if (userInfors.UserId.Equals(user.UserId))
                    {
                        id = userInfors.UserId; break;  
                    }
                }
                if(!string.IsNullOrEmpty(id))
                {
                    SetResponse setResponse = await _firebase._client.SetAsync("UserInfors/" + user.UserId, user);
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_SUCCESS,

                    };
                }
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_NOTFOUND

                };
            }
            catch
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Can not edit to db"

                };
            }
        }
        public async Task<List<UserInfors>> FindUserBySubstringLastNameAsync(string substring)
        {         
            List<UserInfors> matchingUsers = new List<UserInfors>();           
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserInfors");           
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            foreach (var item in jsonResponse)
            {
                var value = item.Value!.ToString();
                
                var user = JsonConvert.DeserializeObject<UserInfors>(value);

                if (user.LastName.ToLower().Contains(substring.ToLower()))
                {                 
                    matchingUsers.Add(user);
                }
            }          
            return matchingUsers;
        }
        public async Task<List<UserInfors>> FindUserBySubstringAddressAsync(string address)
        {           
            List<UserInfors> matchingUsers = new List<UserInfors>();         
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserInfors");      
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();       
            foreach (var item in jsonResponse)
            {
                var value = item.Value!.ToString();
                
                var user = JsonConvert.DeserializeObject<UserInfors>(value);
              
                if (user.Address != null && user.Address.ToLower().Contains(address.ToLower()))
                {                
                    matchingUsers.Add(user);
                }
            }          
            return matchingUsers;
        }
        private int CalculateAge(DateTime birthday)
        {
            DateTime now = DateTime.Today;
            int age = now.Year - birthday.Year;
            if (now < birthday.AddYears(age))
            {
                age--;
            }
            return age;
        }
        public async Task<List<UserInfors>> FindUserByAgeAsync(int age)
        {
            List<UserInfors> matchingUsers = new List<UserInfors>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserInfors");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            foreach (var item in jsonResponse)
            {
                var value = item.Value!.ToString();
                var user = JsonConvert.DeserializeObject<UserInfors>(value);
                int userAge = CalculateAge(user.Birthday);
                if (userAge == age)
                {
                    matchingUsers.Add(user);
                }
            }

            return matchingUsers;
        }
        public async Task<List<UserInfors>> FindUserByLastNameByAddressAndAgeAsync(string input)
        {
            List<UserInfors> matchingUsers = new List<UserInfors>();

            // Phân tích chuỗi đầu vào thành các thành phần: họ, địa chỉ, tuổi
            string[] parts = input.Split(',');

            if (parts.Length != 3)
            {
                throw new ArgumentException("Định dạng đầu vào không hợp lệ. Vui lòng nhập theo định dạng 'LastName, Address, Age'.");
            }

            string lastName = parts[0].Trim();
            string address = parts[1].Trim();
            if (!int.TryParse(parts[2].Trim(), out int age))
            {
                throw new ArgumentException("Tuổi phải là một số nguyên.");
            }

            // Lấy danh sách người dùng theo các điều kiện
            var usersByLastName = await FindUserBySubstringLastNameAsync(lastName);
            var usersByAddress = await FindUserBySubstringAddressAsync(address);
            var usersByAge = await FindUserByAgeAsync(age);

            // Tìm kiếm người dùng thỏa mãn tất cả các điều kiện
            foreach (var userByLastName in usersByLastName)
            {
                if (usersByAddress.Any(u => u.UserId == userByLastName.UserId) &&
                    usersByAge.Any(u => u.UserId == userByLastName.UserId))
                {
                    matchingUsers.Add(userByLastName);
                }
            }

            return matchingUsers;
        }




    }
}
