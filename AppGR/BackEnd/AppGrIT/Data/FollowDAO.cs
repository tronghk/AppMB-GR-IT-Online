using AppGrIT.Entity;
using AppGrIT.Helper;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

namespace AppGrIT.Data
{
    public class FollowDAO
    {
        private readonly ConnectFirebase _firebase;

        public FollowDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<string> CreateFollowAsync(UserFollows fl)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("UserFollows/", fl);
                return StatusResponse.STATUS_SUCCESS;
               
            }
            catch (Exception ex)
            {
                return StatusResponse.STATUS_ERROR;
                return null!;
            }
        }
        public async Task<List<UserFollows>> GetUserFollow(string userId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFollows");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            List<UserFollows> list = new List<UserFollows>();
            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<UserFollows>(value);
                    if (userc.UserId.Equals(userId))
                    {
                        list.Add(userc);
                    }
                }
               


            }
            return list;

        }
        public async Task<string> DeleteUserFollow(UserFollows user)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFollows");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
           
            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<UserFollows>(value);
                    if (userc.UserId.Equals(user.UserId) && userc.UserFollowId.Equals(user.UserFollowId))
                    {
                        var key = item.Key!.ToString();
                        await _firebase._client.DeleteAsync(key);
                        return StatusResponse.STATUS_SUCCESS;
                    }
                }



            }
            return StatusResponse.STATUS_ERROR;

        }
        public async Task<UserFollows> GetUserFollow(string userId, string unUserFollow)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserFollows");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<UserFollows>(value);
                    if (userc.UserId.Equals(userId) && userc.UserFollowId.Equals(unUserFollow))
                    {
                        return userc;
                    }
                }



            }
            return null!;

        }
    }
}
