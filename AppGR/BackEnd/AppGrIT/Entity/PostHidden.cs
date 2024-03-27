using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class PostHidden
    {
        [Key]
        [Required]
        public string UserId
        { get; set; } = null!;
        [Key]
        [Required]
        public string PostId
        { get; set; } = null!;
    }
}
