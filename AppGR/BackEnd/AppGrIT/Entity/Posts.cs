using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class Posts
    {
        [Required]
        [Key]
        public string PostId
        { get; set; } = null!;
        [Required]
        public string UserId
        { get; set; } = null!;

        public string Content
        { get; set; } = null!;

        [Required]
        public DateTime PostTime
        { get; set; }

        [Required]

        public string PostType { get; set; } = null!;





    }
}
