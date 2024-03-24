namespace AppGrIT.Services
{
    public interface IExpression
    {

        public Task<int> CountExpressionInPostFromType(string postId, string expression);
    }
}
