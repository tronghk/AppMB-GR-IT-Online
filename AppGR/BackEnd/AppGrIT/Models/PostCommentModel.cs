using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class PostCommentModel
    {
        [Required]
        public string PostId
        { get; set; } = null!;
        [Required]
        public string UserId

        { get; set; } = null!;
        [Required]
        public string Content

        { get; set; } = null!;
        [Required]
        public DateTime CommentTime

        { get; set; }
    }
}
