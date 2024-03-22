namespace AppGrIT.Models
{
    public class PostCommentsModel
    {

        public string UserId { get; set; } = null!;
        public string UserName
        { get; set; } = null!;
        public string Content
        { get; set; } = null!;
        public List<ExpressionModel> Expressions { get; set; } = new List<ExpressionModel>();

    }
}
