using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Security.Principal;

namespace AppGrIT.Data
{
    public class UserFollowsDAO
    {
        private readonly ConnectFirebase _firebase;
        public UserFollowsDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<int> CountFollowers(string userId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFollows");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            int count = 0;

            // Kiểm tra xem dữ liệu từ Firebase có tồn tại không
            if (jsonResponse != null)
            {
                // Lặp qua từng mục trong dữ liệu
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    var userFollows = JsonConvert.DeserializeObject<UserFollows>(value);

                    // Kiểm tra xem userId có tồn tại trong cấu trúc dữ liệu không
                    if (userFollows != null && userFollows.UserId.Equals(userId))
                    {
                        count++;
                    }
                }
            }

            return count;
        }

        public async Task<int> CountUserFollowers(string userId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFollows");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            int count = 0;

            // Kiểm tra xem dữ liệu từ Firebase có tồn tại không
            if (jsonResponse != null)
            {
                // Lặp qua từng mục trong dữ liệu
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    var userFollows = JsonConvert.DeserializeObject<UserFollows>(value);

                    // Kiểm tra xem userId có tồn tại trong cấu trúc dữ liệu không
                    if (userFollows.UserFollowId.Equals(userId))
                    {
                        count++;
                    }
                }
            }

            return count;
        }

    }

}
