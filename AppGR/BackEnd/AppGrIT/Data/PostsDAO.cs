using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System.Security.Principal;
using Firebase.Auth;

namespace AppGrIT.Data
{
    public class PostsDAO
    {
        private readonly ConnectFirebase _firebase;
        public PostsDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }

        public async Task<Posts> CreatePostAsync(Posts posts)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("Posts/", posts);

                posts.PostId = response.Result.name;
                SetResponse setResponse = await _firebase._client.SetAsync("Posts/" + posts.PostId, posts);
                return posts;
            }
            catch (Exception ex)
            {
                return null!;
            }

        }
        public async Task<PostSellProduct> CreatePostSellAsync(PostSellProduct posts)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("PostSellProduct/", posts);

                posts.PostSellProductId = response.Result.name;
                SetResponse setResponse = await _firebase._client.SetAsync("PostSellProduct/" + posts.PostSellProductId, posts);
                return posts;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }
        public async Task<PostShares> CreatePostShareAsync(PostShares posts)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("PostShares/", posts);
                return posts;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }
        public async Task<PostShares> GetPostShare(string postId, string userId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostShares");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();


            if (jsonResponse != null)
            {
                // lọc bài viết mới
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var post = JsonConvert.DeserializeObject<PostShares>(value);
                    if (post!.UserId.Equals(userId) && post.PostId.Equals(postId))
                    {
                        return post;
                    }

                }
            }
            return null!;
        }
        public async Task<List<PostShares>> GetListPostShare( string userId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostShares");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            List<PostShares> list = new List<PostShares>();

            if (jsonResponse != null)
            {
                // lọc bài viết mới
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var post = JsonConvert.DeserializeObject<PostShares>(value);
                    if (post!.UserId.Equals(userId))
                    {
                        list.Add(post);
                    }

                }
            }
            return list;
        }
        public async Task<Posts> EditPostAsync(Posts posts)
        {
            try
            {
                SetResponse setResponse = await _firebase._client.SetAsync("Posts/" + posts.PostId + "/Content", posts.Content);
                return posts;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }
        public async Task<PostSellProduct> EditPostSellAsync(PostSellProduct posts)
        {
            try
            {
                SetResponse setResponse = await _firebase._client.SetAsync("PostSellProduct/" + posts.PostSellProductId, posts);
                return posts;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }

        public async Task<List<Posts>> GetPostsAsync(List<string> listcmt, List<string> listexp)
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
                    if (!CheckPostValid(post.PostId, listcmt) && !CheckPostValid(post.PostId, listexp) && post.PostType.Equals("1"))
                    {
                        result.Add(post);
                    }

                }
                // lọc bài viết theo random
                if (result.Count < 10)
                {

                    foreach (var item in jsonResponse)
                    {
                        var value = item.Value!.ToString();
                        //path Object
                        var post = JsonConvert.DeserializeObject<Posts>(value);
                        if (CheckPostValid(post.PostId, listcmt) && CheckPostValid(post.PostId, listexp) && post.PostType.Equals("1"))
                        {
                            result.Add(post);
                        }

                    }

                }


            }
            return result;

        }
        public async Task<List<Posts>> GetPostsAvatarAsync(string userId)
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
                    if (post.PostType.Equals("3") && post.UserId.Equals(userId))
                    {
                        result.Add(post);
                    }

                }
                // lọc bài viết theo random
              


            }
            return result;

        }
        public async Task<List<Posts>> GetPostsCoverAsync(string userId)
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
                    if (post!.PostType.Equals("2") && post.UserId.Equals(userId))
                    {
                        result.Add(post);
                    }

                }
                // lọc bài viết theo random



            }
            return result;

        }
        public bool CheckPostValid(string postId, List<string> list)
        {
            foreach (var post in list)
            {
                if (post.Equals(postId))
                    return true;
            }
            return false;
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
        public async Task<List<Posts>> GetPostsSelfAsync(string userId)
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
                    if (post!.UserId.Equals(userId))
                    { result.Add(post); }

                }
            }
            return result;
        }

        public async Task<Posts> GetPosts(string postId) {

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
                    if (post!.PostId.Equals(postId))
                    { return post; }

                }



            }
            return null;
        }
        public async Task<PostSellProduct> GetPostsSell(string postId)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostSellProduct");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();


            if (jsonResponse != null)
            {
                // lọc bài viết mới
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var post = JsonConvert.DeserializeObject<PostSellProduct>(value);
                    if (post!.PostSellProductId.Equals(postId))
                    { return post; }

                }



            }
            return null;
        }
        public async Task<string> DeletePost(string postId)
        {
            try
            {
                await _firebase._client.DeleteAsync("Posts/" + postId);
                return MessageResponse.MESSAGE_DELETE_SUCCESS;
            }
            catch
            {
                return MessageResponse.MESSAGE_DELETE_FAIL;
            }


        }
        public async Task<string> DeletePostSell(string postId)
        {
            try
            {
                await _firebase._client.DeleteAsync("PostSellProduct/" + postId);
                return MessageResponse.MESSAGE_DELETE_SUCCESS;
            }
            catch
            {
                return MessageResponse.MESSAGE_DELETE_FAIL;
            }


        }
        public async Task<string> DeleteSharePostAsync(PostShares posts)
        {

            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostShares");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            try
            {
                if (jsonResponse != null)
                {

                    foreach (var item in jsonResponse)
                    {
                        var value = item.Value!.ToString();
                        //path Object
                        var userc = JsonConvert.DeserializeObject<PostShares>(value);
                        if (userc.UserId.Equals(posts.UserId) && userc.PostId.Equals(posts.PostId)) {
                            var key = item.Key!;
                            await _firebase._client.DeleteAsync(key);
                            return StatusResponse.STATUS_SUCCESS;
                        }
                    }

                }
                

            }
            catch
            {
                return StatusResponse.STATUS_ERROR;
            }
            return StatusResponse.STATUS_ERROR;
        }
        public async Task<string> HiddenPost(PostHidden post)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("PostHidden", post);
                return StatusResponse.STATUS_SUCCESS;
            }
            catch (Exception ex)
            {
                return StatusResponse.STATUS_ERROR;
            }
        }
        public async Task<List<PostHidden>> GetHiddenPost(string userId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostHidden");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            List<PostHidden> list = new List<PostHidden>();
            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<PostHidden>(value);
                    if (userc.UserId.Equals(userId))
                    {
                        list.Add(userc);
                    }
                }



            }
            return list;
        }
        public async Task<bool> CheckPostHidden(string postId, string userId)
        {
            var list = await GetHiddenPost(userId);
            foreach(var item in list) 
            {
                if(item.PostId.Equals(postId))
                    return true;
            }
            return false;
        }
    }
}
