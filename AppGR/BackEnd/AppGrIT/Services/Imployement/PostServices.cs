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
        private readonly ImagesDAO _imageDAO;

        public PostServices(PostsDAO postDAO, IImages imageManger, IPostExpressionss postpre, IPostComments postcmt, ImagesDAO image)
        {
            _postDAO = postDAO;
            _imageManager = imageManger;
            _postCommentManager = postcmt;
            _postExpressionManager = postpre;
            _imageDAO = image;
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
            List<ImagePostModel> images = new List<ImagePostModel>();
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
        public async Task<PostModel> EditPostAsync(PostModel model)
        {
            var post = new Posts
            {
                PostId = model.PostId!,
                UserId = model.UserId!,
                Content = model.Content!,
                PostTime = model.PostTime,
                PostType = model.PostType!,
            };
            if (!await CheckContent(model.Content!, model.PostId!))
            {
                var p = await _postDAO.EditPostAsync(post);
                post = p;
            }

            

            var list = model.imagePost;
            var listPost = await _imageManager.GetImagePostToId(model.PostId!);
            foreach ( var imagePost in list )
            {
                var result = CheckImageExsist(imagePost, listPost);
                if (result != null)
                {
                    if (!CheckImageContentDup(result, imagePost))
                    {
                        // khác content
                        var postImage = new PostImages
                        {
                            PostImageId = result.ImageId!,
                            ImageContent = imagePost.ImageContent!,
                            ImagePath = result.ImagePath!,
                            PostId = model.PostId!
                        };
                        await _imageDAO.UpdateImageAsync(postImage);
                    }
                    if(!CheckImagePathDup(result, imagePost))
                    {
                        // xóa hình trong storage
                        await _imageManager.DeleteFileStorageToPath(result.ImagePath!);

                        // cập nhật path mới
                        var postImage = new PostImages
                        {
                            PostImageId = result.ImageId!,
                            ImageContent = result.ImageContent!,
                            ImagePath = imagePost.ImagePath!,
                            PostId = model.PostId!
                        };
                        await _imageDAO.UpdateImageAsync(postImage);
                    }
                    // xóa khỏi danh sách chờ
                    int index = FindIndexImageInList(result.ImageId!, listPost);
                    listPost.RemoveAt(index);
                }
                else
                {
                    // tạo mới hình ảnh
                    var postImage = new PostImages
                    {
                        ImageContent = imagePost.ImageContent!,
                        ImagePath = imagePost.ImagePath!,
                        PostId = model.PostId!
                    };
                    await _imageDAO.CreateImageAsync(postImage);
                }
            }
            // xóa những image cũ còn tồn tại trong db
            foreach(var value in listPost)
            {
                // xóa hình trong storage
                await _imageManager.DeleteFileStorageToPath(value.ImagePath!);
                // xóa db
                await _imageDAO.DeleteImagePost(value.ImageId!);
            }
            var listImage = await _imageManager.GetImagePostToId(model.PostId!);
            var postModel = new PostModel
            {
                PostId = post.PostId,
                UserId = post.UserId!,
                Content = post.Content!,
                PostTime = post.PostTime,
                PostType = post.PostType!,
                imagePost = listImage
            };
            return postModel;
        }
        public async Task<ResponseModel> DeletePostAsync(string postId)
        {
           
           
            var listPost = await _imageManager.GetImagePostToId(postId);
          
            
            // xóa những image cũ còn tồn tại trong db
            foreach (var value in listPost)
            {
                // xóa hình trong storage
                await _imageManager.DeleteFileStorageToPath(value.ImagePath!);
                // xóa db
                await _imageDAO.DeleteImagePost(value.ImageId!);
            }
            var result = await _postDAO.DeletePost(postId);
            if (result.Equals(MessageResponse.MESSAGE_DELETE_SUCCESS))
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = result
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = result
            };
        }
        public int FindIndexImageInList(string imageId, List<ImagePostModel> list)
        {
            int index = 0;
            foreach (var value in list)
            {
                if (value.ImageId!.Equals(imageId))
                    return index;
                index++;
            }
            return -1;
        }
        public ImagePostModel CheckImageExsist(ImagePostModel imagePost, List<ImagePostModel> list)
        {
           
            foreach(var value in list)
            {
                if (value.ImageId!.Equals(imagePost.ImageId))
                        return value;
            }
            return null!;
        }
        public bool CheckImageContentDup(ImagePostModel imagePostNew, ImagePostModel imagePostOld)
        {
            if(imagePostNew.ImageContent!.Equals(imagePostOld.ImageContent))
                return true;
            return false;
        }
        public bool CheckImagePathDup(ImagePostModel imagePostNew, ImagePostModel imagePostOld)
        {
            if (imagePostNew.ImagePath!.Equals(imagePostOld.ImagePath))
                return true;
            return false;
        }
        public async Task<bool> CheckContent(string content, string postId)
        {
            var post = await _postDAO.GetPosts(postId);
            if (post.Content.Equals(content))
                return true;
            return false;
        }

        public async Task<List<PostModel>> GetListPostUser(string userId)
        {
            List<string> listComt = await _postCommentManager.GetPostIdToUserFromPostComment(userId);
            List<string> listExp = await _postExpressionManager.GetPostIdToUserFromPostExpression(userId);
            var result = await _postDAO.GetPostsAsync(listComt, listExp);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
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
            return list;
        }
        public async Task<List<PostModel>> GetListAvatarPostUser(string userId)
        {
            var result = await _postDAO.GetPostsAvatarAsync(userId);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
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
            return list;
        }
        public async Task<List<PostModel>> GetListCoverPostUser(string userId)
        {
            var result = await _postDAO.GetPostsCoverAsync(userId);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
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
            return list;
        }
        public async Task<PostModel> GetPostNewInfoUser(string userId)
        {
            var list = await _postDAO.GetUserInstead(userId);

            var idNew = list[0];
            foreach (var post in list)
            {
                if (post.PostTime > idNew.PostTime)
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

        public async Task<PostModel> FindPostToIdAsync(string postId)
        {
            var idNew = await _postDAO.GetPosts(postId);
            if (idNew == null)
                return null;
            var listImage = await _imageManager.GetImagePostToId(idNew.PostId);
            var postModel = new PostModel
            {
                PostId = idNew.PostId,
                Content = idNew.Content,
                PostTime = idNew.PostTime,
                PostType = idNew.PostType,
                UserId = idNew.UserId!,
                imagePost = listImage
            };
            return postModel;
        }
    }
}
