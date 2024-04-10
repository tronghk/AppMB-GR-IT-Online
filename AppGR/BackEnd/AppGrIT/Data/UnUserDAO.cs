using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using AppGrIT.Helper;

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
        public async Task<List<UnUser>> GetListUnUser(string userId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UnUser");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            List<UnUser> list = new List<UnUser>();
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<UnUser>(value);
                    if (userc.UserId.Equals(userId))
                    {
                        list.Add(userc);
                    }
                }
            }
            return list;

        }
        public async Task<string> CreateUnUserAsync(UnUser un)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("UnUser/", un);
                return StatusResponse.STATUS_SUCCESS;

            }
            catch (Exception ex)
            {
                return StatusResponse.STATUS_ERROR;
                return null!;
            }
        }

        public async Task<UnUser> GetUnUser(string userId, string unUserFriend)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UnUser");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<UnUser>(value);
                    if (userc.UserId.Equals(userId) && userc.UnUserId.Equals(unUserFriend))
                    {
                        return userc;
                    }
                }
            }
            return null!;

        }

    }
}

