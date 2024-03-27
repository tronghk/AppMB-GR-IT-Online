using AppGrIT.Data;
using AppGrIT.Entity;
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
    }
}
