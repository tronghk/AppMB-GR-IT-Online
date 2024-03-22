using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class PostComments
    {
        [Required]
        [Key]
        public string CommentId
        { get; set; } = null!;
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
