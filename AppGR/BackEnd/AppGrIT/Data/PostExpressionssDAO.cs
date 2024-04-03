using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using AppGrIT.Helper;

namespace AppGrIT.Data
{

    public class PostExpressionssDAO
    {
        private readonly ConnectFirebase _firebase;
        public PostExpressionssDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<List<string>> GetPostIdToUserFromPostExpression(string userId)
        {
            List<string> postIds = new List<string>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostExpressions");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<PostExpressions>(value);
                    if (userc.UserId.Equals(userId))
                    {
                        postIds.Add(userc.PostId);
                    }
                }


            }
            return postIds;

        }
        public async Task<List<PostExpressions>> GetPostExpression(string postId)
        {
            List<PostExpressions> postIds = new List<PostExpressions>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostExpressions");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<PostExpressions>(value);
                    if (userc.PostId.Equals(postId))
                    {
                        postIds.Add(userc);
                    }
                }


            }
            return postIds;

        }
        public async Task<List<CommentExpressions>> GetCommentPostExpression(string postId, string cmt, string userId)
        {
            List<CommentExpressions> postIds = new List<CommentExpressions>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("CommentExpressions");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<CommentExpressions>(value);
                    if (userc.PostId.Equals(postId) && userc.CommentId.Equals(cmt) && userc.UserId.Equals(userId))
                    {
                        postIds.Add(userc);
                    }
                }


            }
            return postIds;

        }
        public async Task<PostExpressions> CreatePostExpressionAsync(PostExpressions posts)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("PostExpressions/", posts);
                return posts;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }
        public async Task<CommentExpressions> CreatePostCommentExpressionAsync(CommentExpressions posts)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("CommentExpressions/", posts);
                return posts;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }
        public async Task<string> DeletePostCommentExpressionAsync(CommentExpressions posts)
        {
           
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("CommentExpressions");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            try
            {
                if (jsonResponse != null)
                {

                    foreach (var item in jsonResponse)
                    {
                        var value = item.Value!.ToString();
                        //path Object
                        var userc = JsonConvert.DeserializeObject<CommentExpressions>(value);
                        if (userc.UserId.Equals(posts.UserId) && userc.CommentId.Equals(posts.CommentId) && userc.PostId.Equals(posts.PostId))
                        {
                            var key = item.Key;
                            await _firebase._client.DeleteAsync("CommentExpressions/"+key);
                            break;
                        }
                    }
                    return StatusResponse.STATUS_SUCCESS;
                }
               
            }
            catch
            {
                return StatusResponse.STATUS_ERROR;
            }
            return StatusResponse.STATUS_ERROR;

        }
        public async Task<string> DeletePostExpressionAsync(PostExpressions posts)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostExpressions");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            try
            {
                if (jsonResponse != null)
                {

                    foreach (var item in jsonResponse)
                    {
                        var value = item.Value!.ToString();
                       
                        //path Object
                        var userc = JsonConvert.DeserializeObject<PostExpressions>(value);
                        if (userc.UserId.Equals(posts.UserId) && userc.PostId.Equals(posts.PostId))
                        {
                            var key = item.Key;
                            await _firebase._client.DeleteAsync("PostExpressions/"+key);
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


    }
}
