using AppGrIT.Entity;
using AppGrIT.Model;
using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace AppGrIT.Data
{
    public class UserDAO
    {
        public ConnectFirebase _firebase;
        public UserDAO(ConnectFirebase connectFirebase) {
            _firebase = connectFirebase;
        }
       
        public async Task<ResponseModel> AddUserFirebase(AccountIdentity account)
        {
            try
            {
                PushResponse response = await _firebase._client.PushAsync("Users/", account);
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
        public async Task<List<AccountIdentity>> GetUserFirebase()
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
    }
}
