namespace AppGrIT.Models
{
    public class PostModel
    {
        public string PostId
        { get; set; } = null!;

        public DateTime PostTime { get; set; }



        public string Content
        { get; set; } = null!;

        public List<ExpressionModel> Expressions { get; set; } = new List<ExpressionModel>();

        public List<ImagePostModel> ImagePosts { get; set; } = new List<ImagePostModel>();
    }
}
