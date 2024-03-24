using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services.Imployement
{
    public class PostServices : IPosts
    {
        private readonly PostsDAO _postDAO;
        private readonly ImagesDAO _imageDAO;
        private readonly IImages _imageManager;

        public PostServices(PostsDAO postDAO, ImagesDAO image, IImages imageManger) {
            _postDAO = postDAO;
            _imageDAO = image;
            _imageManager = imageManger;
        }
        public async Task<PostModel> CreatePostAsync(PostModel model)
        {
            var post = new Posts
            {
                UserId = model.UserId!,
                Content = model.Content!,
                PostTime = model.PostTime,
                PostType = model.PostType!,
            };
            var p = await _postDAO.CreatePostAsync(post);

            var postModel = new PostModel
            {
                PostId = p.PostId,
                UserId = p.UserId!,
                Content = p.Content!,
                PostTime = p.PostTime,
                PostType = p.PostType!,
            };
            return postModel;
         }
         

        
        
    }
}
