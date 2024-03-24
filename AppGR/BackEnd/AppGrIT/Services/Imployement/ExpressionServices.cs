
using AppGrIT.Data;

namespace AppGrIT.Services.Imployement
{
    public class ExpressionServices : IExpression
    {
        private readonly ExpressionDAO _expressionDAO;

        public ExpressionServices(ExpressionDAO expression) {
            _expressionDAO = expression;
        }
        public async Task<int> CountExpressionInPostFromType(string postId, string expression)
        {
            var countExpression = await _expressionDAO.CountExpressionInPost(postId, expression);
            return countExpression;
        }
    }
}
