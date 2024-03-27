
using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Models;
using Microsoft.AspNetCore.Identity;

namespace AppGrIT.Services.Imployement
{
    public class PostCommentServices : IPostComments
    {
        private readonly PostCommentsDAO _postcmtDAO;
        private readonly IUsers _userManager;

        public PostCommentServices(PostCommentsDAO post, IUsers user)
        {
            _postcmtDAO = post;
            _userManager = user;
        }

        public async Task<PostCommentModel> CreatePostCommentAsync(PostCommentModel model)
        {
            var postCmt = new PostComments
            {
                PostId = model.PostId,
                UserId = model.UserId,
                Content = model.Content,
                CommentTime = model.CommentTime,

            };
            var result = await _postcmtDAO.CreatePostCommentAsync(postCmt);
            var icon = new PostCommentModel
            {
                CommentId = result.CommentId,
                UserId = result.UserId,
                PostId = result.PostId,
                CommentTime = result.CommentTime
                   ,
                Content = result.Content


            };
            var user = await _userManager.GetInfoUser(result.UserId);
            icon.UserName = user.UserName;
            icon.UserImage = user.ImagePath;
            return icon;
        }

        public async Task<List<PostCommentModel>> GetPostComment(string postId)
        {
           var result = await _postcmtDAO.GetPostComment(postId);
            var list = new List<PostCommentModel>();
            foreach (var item in result)
            {
                var icon = new PostCommentModel
                {
                    UserId = item.UserId,
                    PostId = postId,
                    CommentTime = item.CommentTime
                    , Content = item.Content


                };
                var user = await _userManager.GetInfoUser(item.UserId);
                icon.UserName = user.UserName;
                icon.UserImage = user.ImagePath;
                list.Add(icon);
            }
            return list;
        }

        public async Task<List<string>> GetPostIdToUserFromPostComment(string userId)
        {
            return await _postcmtDAO.GetPostIdToUserFromPostComment(userId);
        }
    }
}
