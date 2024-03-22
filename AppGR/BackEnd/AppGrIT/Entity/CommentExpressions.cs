using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class CommentExpressions
    {
        [Required]
        [Key]
        public string PostId
        { get; set; } = null!;

        [Required]
        [Key]
        public string UserId
        { get; set; } = null!;
        [Required]
        [Key]
        public string CommentId
        { get; set; } = null!;
        [Required]
        public int Expression
        { get; set; }
    }
}
