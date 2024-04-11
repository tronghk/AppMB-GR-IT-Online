using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using Firebase.Auth;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

namespace AppGrIT.Data
{
    public class PaymentDAO
    {
        private readonly ConnectFirebase _firebase;
        public PaymentDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }

        public async Task<string> CreateBill(Bills bill)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("Bills/", bill);
                return StatusResponse.STATUS_SUCCESS;

            }
            catch (Exception ex)
            {
                return StatusResponse.STATUS_ERROR;
                
            }
        }
        public async Task<string> CreateUserSell(UserSells userSell)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("UserSells/", userSell);
                return StatusResponse.STATUS_SUCCESS;

            }
            catch (Exception ex)
            {
                return StatusResponse.STATUS_ERROR;

            }
        }
        public async Task<string> EditUserSellAsync(UserSells userSell)
        {
            FirebaseResponse response = await _firebase._client.GetAsync("UserSells");
            //path All Id
            JObject jsonResponse = response.ResultAs<JObject>();
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var us = JsonConvert.DeserializeObject<UserSells>(value);
                    if (us.UserId == userSell.UserId)
                    {

                        var key = item.Key;
                        await _firebase._client.UpdateAsync("UserSells/" + key, userSell);
                        return StatusResponse.STATUS_SUCCESS;
                    }
                }



            }
            return StatusResponse.STATUS_ERROR;
        }
        public async Task<UserSells> GetUserSell(string userId)
        {
            FirebaseResponse response = await _firebase._client.GetAsync("UserSells");
            //path All Id
            JObject jsonResponse = response.ResultAs<JObject>();
           if(jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var us = JsonConvert.DeserializeObject<UserSells>(value);
                    if (us.UserId == userId)
                    {

                        return us;
                    }
                }
                

                
            }
            return null;
        }
    }
}
