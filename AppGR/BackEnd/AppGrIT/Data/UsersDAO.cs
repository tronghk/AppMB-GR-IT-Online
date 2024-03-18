using AppGrIT.Entity;
using AppGrIT.Model;
using Firebase.Auth;
using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
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
                    Status = "Ok",
                    Message = "Register success"
                    
                };
            }catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status =  "Fail",
                    Message = ex.Message
                };
            }

           
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
            return null!;

        }


    }
}
