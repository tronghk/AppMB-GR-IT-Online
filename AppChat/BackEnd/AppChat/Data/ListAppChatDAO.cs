using AppGrIT.Data;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using AppGrIT.Models;
using AppChat.Models;
using AppChat.Entity;

namespace AppChat.Data
{
    public class ListAppChatDAO
    {
        private readonly ConnectFirebase _firebase;
        public ListAppChatDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<List<ChatSegMent>> GetListMess(string userId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("ChatSegMent");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            List<ChatSegMent> list = new List<ChatSegMent>();
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    
                    var userc = JsonConvert.DeserializeObject<ChatSegMent>(value);
                    if (userc.UserId.Equals(userId))
                    {
                        list.Add(userc);
                    }
                }
            }
            return list;

        }
        public async Task<UserInfos> GetUserInforAsync(string UserId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserInfors");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();          
            foreach (var item in jsonResponse)
            {
                var value = item.Value!.ToString();               
                UserInfos userInfors = JsonConvert.DeserializeObject<UserInfos>(value);              
                if (userInfors.UserId.Equals(UserId))
                {
                    return userInfors; 
                }
            }
            return null; 
        }

        public async Task<List<Posts>> GetUserInstead(string userId)
        {
            List<Posts> result = new List<Posts>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Posts");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();


            if (jsonResponse != null)
            {
                // lọc bài viết mới
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var post = JsonConvert.DeserializeObject<Posts>(value);
                    if (post.UserId.Equals(userId) && post.PostType.Equals("3"))
                    { result.Add(post); }

                }



            }
            return result;
        }
        public async Task<List<PostImage>> GetImagePostToId(string postId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostImages");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            var result = new List<PostImage>();

            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse!)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var im = JsonConvert.DeserializeObject<PostImage>(value);
                    if (im.PostId.Equals(postId))
                    {
                        result.Add(im);
                    }
                }
            }
            return result;
        }

        public async Task<List<DetailsChat>> GetListDetailschat(string chatId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("DetailsChat");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            List<DetailsChat> list = new List<DetailsChat>();
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();

                    var userc = JsonConvert.DeserializeObject<DetailsChat>(value);
                    if (userc.ChatId.Equals(chatId))
                    {
                        list.Add(userc);
                    }
                }
            }
            return list;

        }

    }
}
