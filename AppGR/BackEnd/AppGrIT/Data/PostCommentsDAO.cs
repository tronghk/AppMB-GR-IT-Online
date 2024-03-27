using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;

namespace AppGrIT.Data
{
    public class PostCommentsDAO
    {
        private readonly ConnectFirebase _firebase;
        public PostCommentsDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }

        public async Task<List<string>> GetPostIdToUserFromPostComment(string userId)
        {
            List<string> postIds = new List<string>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostComments");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
          
            if(jsonResponse != null)
            {
               
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<PostComments>(value);
                    if (userc.UserId.Equals(userId))
                    {
                        postIds.Add(userc.PostId);
                    }
                }
                
               
            }
            return postIds;
           
        }
        public async Task<List<PostComments>> GetPostComment(string postId)
        {
            List<PostComments> postIds = new List<PostComments>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostComments");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();

            if (jsonResponse != null)
            {

                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var userc = JsonConvert.DeserializeObject<PostComments>(value);
                    if (userc!.PostId.Equals(postId))
                    {
                        postIds.Add(userc);
                    }
                }


            }
            return postIds;

        }
        public async Task<PostComments> CreatePostCommentAsync(PostComments posts)
        {
            try
            {

                PushResponse response = await _firebase._client.PushAsync("PostComments/", posts);

                posts.PostId = response.Result.name;
                SetResponse setResponse = await _firebase._client.SetAsync("PostComments/" + posts.PostId, posts);
                return posts;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }
    }
}
