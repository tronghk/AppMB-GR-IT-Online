using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services.Imployement
{
    public class PostExpressionServicess : IPostExpressionss
    {
        private readonly PostExpressionssDAO _postExpressionDAO;
        private readonly IUsers _userManager;

        public PostExpressionServicess(PostExpressionssDAO post, IUsers user)
        {
            _postExpressionDAO = post;
            _userManager = user;
        }

        public async Task<List<PostIconsModel>> GetPostExpression(string postId)
        {
            var result = await _postExpressionDAO.GetPostExpression(postId);
            var list = new List<PostIconsModel>();
            foreach (var item in result)
            {
                var icon = new PostIconsModel
                {
                    UserId = item.UserId,
                    Expression = item.Expression,


                };
                var user = await _userManager.GetInfoUser(item.UserId);
                icon.UserName = user.UserName;
                icon.UserImage = user.ImagePath;
                list.Add(icon);
            }
            return list;
        }

        public async Task<List<string>> GetPostIdToUserFromPostExpression(string userId)
        {
            return await _postExpressionDAO.GetPostIdToUserFromPostExpression(userId);
        }
        public async Task<ExpressionModel> CreateExpressionAsync(ExpressionModel model)
        {
            var postCmt = new PostExpressions
            {
                PostId = model.PostId,
                UserId = model.UserId,
                Expression = model.Expression,

            };
            if (model.Type.Equals("1"))
            {
                var result = await _postExpressionDAO.CreatePostExpressionAsync(postCmt);
                var icon = new ExpressionModel
                {

                    UserId = result.UserId,
                    PostId = result.PostId,
                    Expression = result.Expression,
                    Type = "1"
                };
                return icon;
            }
            else
            {
                var expressionCmt = new CommentExpressions
                {
                    PostId = model.PostId,
                    UserId = model.UserId,
                    CommentId = model.CommentId!,
                    Expression = model.Expression,

                };
                var result = await _postExpressionDAO.CreatePostCommentExpressionAsync(expressionCmt);
                var icon = new ExpressionModel
                {

                    UserId = result.UserId,
                    PostId = result.PostId,
                    CommentId = result.CommentId!,
                    Expression = result.Expression,
                    Type = "2"
                };
                return icon;

            }
        
            
           
        }

        public async Task<ResponseModel> DeleteExpressionAsync(ExpressionModel model)
        {
            var postCmt = new PostExpressions
            {
                PostId = model.PostId,
                UserId = model.UserId,
                Expression = model.Expression,

            };
            if (model.Type.Equals("1"))
            {
                var result = await _postExpressionDAO.DeletePostExpressionAsync(postCmt);
                if (result.Equals(StatusResponse.STATUS_SUCCESS))
                {
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_SUCCESS,
                        Message = MessageResponse.MESSAGE_DELETE_SUCCESS,
                    };
                }
            }
            else
            {
                var expressionCmt = new CommentExpressions
                {
                    PostId = model.PostId,
                    UserId = model.UserId,
                    CommentId = model.CommentId!,
                    Expression = model.Expression,

                };
                var result = await _postExpressionDAO.DeletePostCommentExpressionAsync(expressionCmt);
                if (result.Equals(StatusResponse.STATUS_SUCCESS))
                {
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_SUCCESS,
                        Message = MessageResponse.MESSAGE_DELETE_SUCCESS,
                    };
                }

            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_DELETE_FAIL,
            };
        }
    }
}
