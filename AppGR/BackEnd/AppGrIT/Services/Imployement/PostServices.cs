using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using static System.Net.Mime.MediaTypeNames;

namespace AppGrIT.Services.Imployement
{
    public class PostServices : IPosts
    {
        private readonly PostsDAO _postDAO;
       
        private readonly IImages _imageManager;
        private readonly IPostComments _postCommentManager;
        private readonly IPostExpressionss _postExpressionManager;

        public PostServices(PostsDAO postDAO, IImages imageManger, IPostComments postcmt, IPostExpressionss postpre) {
            _postDAO = postDAO;
            _imageManager = imageManger;
            _postCommentManager = postcmt;
            _postExpressionManager = postpre;
        }

        public async Task<PostModel> EditImagePostInstead(PostModel model)
        {
            var post = new Posts
            {
                PostId = model.PostId!,
                UserId = model.UserId!,
                Content = model.Content!,
                PostTime = model.PostTime,
                PostType = model.PostType!,
            };
            var p = await _postDAO.EditPostAsync(post);
            var postModel = new PostModel
            {
                PostId = p.PostId,
                UserId = p.UserId!,
                Content = p.Content!,
                PostTime = p.PostTime,
                PostType = p.PostType!,
                imagePost = model.imagePost
            };

            return postModel;
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

            var list = model.imagePost;
            List <ImagePostModel> images = new List <ImagePostModel>();
            foreach (var image in list)
            {
               var imageP = await _imageManager.CreateImagePost(image, p.PostId);
                var im = new ImagePostModel
                {
                    ImagePath = imageP.ImagePath,
                    ImageContent = imageP.ImageContent,
                    ImageId = imageP.PostImageId,

                };
                images.Add(im);
            }

            var postModel = new PostModel
            {
                PostId = p.PostId,
                UserId = p.UserId!,
                Content = p.Content!,
                PostTime = p.PostTime,
                PostType = p.PostType!,
                imagePost = images
            };
            
            return postModel;
         }

        public async Task<List<PostModel>> GetListPostUser(string userId)
        {
            List<string> listComt = await _postCommentManager.GetPostIdToUserFromPostComment(userId);
            List<string> listExp = await _postExpressionManager.GetPostIdToUserFromPostExpression(userId);
             var result = await _postDAO.GetPostsAsync(listComt, listExp);
            List<PostModel> list = new List<PostModel>();   
            foreach(var post in result)
            {

                var p = new PostModel
                {
                    PostId = post.PostId,
                    Content = post.Content,
                    PostTime = post.PostTime,
                    PostType = post.PostType,
                    UserId= post.UserId!,
                };

                var listImage = await _imageManager.GetImagePostToId(p.PostId);
                p.imagePost = listImage;
                list.Add(p);
            }
            return list;
        }
        public async Task<List<PostModel>> GetListAvatarPostUser(string userId)
        {
            List<string> listComt = await _postCommentManager.GetPostIdToUserFromPostComment(userId);
            List<string> listExp = await _postExpressionManager.GetPostIdToUserFromPostExpression(userId);
            var result = await _postDAO.GetPostsAsync(listComt, listExp);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
            {
                if (post.PostType.Equals("3"))
                {
                    var p = new PostModel
                    {
                        PostId = post.PostId,
                        Content = post.Content,
                        PostTime = post.PostTime,
                        PostType = post.PostType,
                        UserId = post.UserId!,
                    };

                    var listImage = await _imageManager.GetImagePostToId(p.PostId);
                    p.imagePost = listImage;
                    list.Add(p);
                }
              
            }
            return list;
        }
        public async Task<List<PostModel>> GetListCoverPostUser(string userId)
        {
            List<string> listComt = await _postCommentManager.GetPostIdToUserFromPostComment(userId);
            List<string> listExp = await _postExpressionManager.GetPostIdToUserFromPostExpression(userId);
            var result = await _postDAO.GetPostsAsync(listComt, listExp);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
            {
                if (post.PostType.Equals("2"))
                {
                    var p = new PostModel
                    {
                        PostId = post.PostId,
                        Content = post.Content,
                        PostTime = post.PostTime,
                        PostType = post.PostType,
                        UserId = post.UserId!,
                    };

                    var listImage = await _imageManager.GetImagePostToId(p.PostId);
                    p.imagePost = listImage;
                    list.Add(p);
                }

            }
            return list;
        }
        public async Task<PostModel> GetPostNewInfoUser(string userId)
        {
            var list = await _postDAO.GetUserInstead(userId);

            var idNew = list[0];
            foreach(var post in list)
            {
                if(post.PostTime > idNew.PostTime)
                {
                    idNew = post; break;
                }
            }
            var listImage = await _imageManager.GetImagePostToId(idNew.PostId);

            return new PostModel
            {
                PostId = idNew.PostId,
                Content = idNew.Content,
                PostTime = idNew.PostTime,
                PostType = idNew.PostType,
                UserId = idNew.UserId!,
                imagePost = listImage
            };

        }

        
        
    }
}
