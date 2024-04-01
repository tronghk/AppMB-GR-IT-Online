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

            // Kiểm tra xem dữ liệu từ Firebase có tồn tại không
            if (jsonResponse != null)
            {
                // Lặp qua từng mục trong dữ liệu
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    var userFriends = JsonConvert.DeserializeObject<UserFriends>(value);

                    // Kiểm tra xem userId có tồn tại trong cấu trúc dữ liệu không
                    if (userFriends != null && userFriends.UserId.Equals(userId))
                    {
                        count++;
                    }
                }
            }

            return count;
        }
    }
}
