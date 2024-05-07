using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using AppGrIT.Helper;
using System.Collections.Generic;
using Firebase.Auth;

namespace AppGrIT.Data
{
    public class UserFriendsDAO
    {
        private readonly ConnectFirebase _firebase;
        public UserFriendsDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<int> CountUserFriends(string userId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFriends");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            int count = 0;
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    var userFriends = JsonConvert.DeserializeObject<UserFriends>(value);                
                    if (userFriends != null && userFriends.UserId.Equals(userId))
                    {
                        count++;
                    }
                }
            }

            return count;
        }
        public async Task<List<UserFriends>> GetListUserFriends(string userId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFriends");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            List<UserFriends> list = new List<UserFriends>();
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<UserFriends>(value);
                    if (userc.UserId.Equals(userId))
                    {
                        list.Add(userc);
                        
                    }
                    if (userc.UserFriendId == userId)
                    {
                        var a = userc.UserId;
                        var b = userc.UserFriendId;
                        userc.UserId = b;
                        userc.UserFriendId = a;
                        list.Add(userc);

                    }
                }
            }
            return list;

        }
        public async Task<string> CreateFriendAsync(UserFriends fr)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("UserFriends/", fr);
                return StatusResponse.STATUS_SUCCESS;

            }
            catch (Exception ex)
            {
                return StatusResponse.STATUS_ERROR;
                return null!;
            }
        }
        public async Task<string> UpdateFriendAsync(UserFriends fr)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFriends");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<UserFriends>(value);
                    if (userc.UserId.Equals(fr.UserId) && userc.UserFriendId == fr.UserFriendId)
                    {
                        userc.Status = "True";
                        var key = item.Key;
                        SetResponse setResponse = await _firebase._client.SetAsync("UserFriends/" + key, userc);


                    }
                    if (userc.UserFriendId.Equals(fr.UserId) && userc.UserId == fr.UserFriendId)
                    {
                        userc.Status = "True";
                        var key = item.Key;
                        SetResponse setResponse = await _firebase._client.SetAsync("UserFriends/" + key, userc);

                    }
                }
            }
            return StatusResponse.STATUS_SUCCESS;
        }

        public async Task<UserFriends> GetUserFriend(string userId, string unUserFriend)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFriends");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<UserFriends>(value);
                    if (userc.UserId.Equals(userId) && userc.UserFriendId.Equals(unUserFriend))
                    {
                       
                            
                            return userc;
                       
                        
                    }
                    if (userc.UserFriendId == userId && userc.UserId == unUserFriend)
                    {
                            var a = userc.UserId;
                            var b = userc.UserFriendId;
                            userc.UserId = b;
                            userc.UserFriendId = a;
                            return userc;
                    }
                }
            }
            return null!;

        }

        public async Task<string> DeleteUserFriend(UserFriends user)
        {
            try
            {
                
                FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFriends");
                JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

                if (jsonResponse != null)
                {
                    foreach (var item in jsonResponse)
                    {
                        var value = item.Value!.ToString();
                        
                        var userc = JsonConvert.DeserializeObject<UserFriends>(value);
                        if (userc.UserId.Equals(user.UserId) && userc.UserFriendId.Equals(user.UserFriendId))
                        {
                            var key = item.Key!.ToString();
                            
                            await _firebase._client.DeleteAsync($"UserFriends/{key}");
                            return StatusResponse.STATUS_SUCCESS;
                        }
                    }
                }
                
                return StatusResponse.STATUS_ERROR;
            }
            catch (Exception ex)
            {
               
                return StatusResponse.STATUS_ERROR;
            }
        }

    }
}
