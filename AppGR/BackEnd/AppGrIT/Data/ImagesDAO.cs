using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using FireSharp.Response;

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
    }
}
