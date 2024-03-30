using AppGrIT.Entity;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IPostExpressionss
    {
      public Task<List<string>> GetPostIdToUserFromPostExpression(string userId);
      public Task<List<PostIconsModel>> GetPostExpression(string postId);
        public Task<ExpressionModel> CreateExpressionAsync(ExpressionModel model);
    }
}
