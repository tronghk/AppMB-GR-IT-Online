using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using Firebase.Auth;
using FireSharp.Response;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Security.Principal;

namespace AppGrIT.Data
{
    public class ImagesDAO
    {
        private readonly ConnectFirebase _firebase;

        public ImagesDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<PostImages> CreateImageAsync(PostImages postImage)
        {
            try
            {
                PushResponse response = await _firebase._client.PushAsync("PostImages/", postImage);

                postImage.PostImageId = response.Result.name;
                SetResponse setResponse = await _firebase._client.SetAsync("PostImages/" + postImage.PostImageId, postImage);
                return postImage;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }

        public async Task<PostImages> UpdateImageAsync(PostImages postImage)
        {
            try
            {
                SetResponse setResponse = await _firebase._client.SetAsync("PostImages/" + postImage.PostImageId, postImage);
                return postImage;
            }
            catch (Exception ex)
            {
                return null!;
            }
        }

        public async Task<List<PostImages>> GetImagePostToId(string postId)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostImages");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            var result = new List<PostImages>();
          
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse!)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var im = JsonConvert.DeserializeObject<PostImages>(value);
                    if (im.PostId.Equals(postId))
                    {
                        result.Add(im);
                    }
                }
            }
            return result;
        }
        public async Task<int> GetCountImage()
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostImages");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            if(jsonResponse == null)
            {
                return 0;
            }
            return jsonResponse.Count;
        }
    }
}
