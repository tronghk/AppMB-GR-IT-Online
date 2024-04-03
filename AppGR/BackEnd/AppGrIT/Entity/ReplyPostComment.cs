using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class ReplyPostComment
    {
        [Required]
        public string PostId { get; set; } = null!;
        [Required]
        public string CommentId { get; set; } = null!;
        [Required]
        public string UserId
        { get; set; } = null!;

        [Required]
        [Key]
        public string ReplyCommentId
        { get; set; } = null!;
        [Required]
        public string Content { get; set; } = null!;


    }
}
