using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using FireSharp.Response;
using System.Security.Principal;

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
    }
}
