using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

namespace AppGrIT.Data
{
    public class UnUserDAO
    {
        private readonly ConnectFirebase _firebase;
        public UnUserDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<int> CountUnUser(string userId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UnUser");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            int count = 0;

            // Kiểm tra xem dữ liệu từ Firebase có tồn tại không
            if (jsonResponse != null)
            {
                // Lặp qua từng mục trong dữ liệu
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    var unUser = JsonConvert.DeserializeObject<UnUser>(value);

                    // Kiểm tra xem userId có tồn tại trong cấu trúc dữ liệu không
                    if (unUser != null && unUser.UserId.Equals(userId))
                    {
                        count++;
                    }
                }
            }

            return count;
        }

    }
}

