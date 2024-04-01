using AppGrIT.Entity;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IPostExpressionss
    {
      public Task<List<string>> GetPostIdToUserFromPostExpression(string userId);
      public Task<List<PostIconsModel>> GetPostExpression(string postId);
      public Task<ExpressionModel> CreateExpressionAsync(ExpressionModel model);
        public Task<ResponseModel> DeleteExpressionAsync(ExpressionModel model);
    }
}
