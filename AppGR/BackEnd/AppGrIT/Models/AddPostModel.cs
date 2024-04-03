using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class AddPostModel
    {
        [Required]
        public string UserId { get; set; } = null!;

        [Required]
        public string Content
        { get; set; } = null!;

        [Required]
        public DateTime PostTime
        { get; set; }
    }
}
