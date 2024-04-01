using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

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
                }
            }
            return list;

        }


    }
}
