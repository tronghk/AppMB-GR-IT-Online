using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class SharePostModel
    {
        [Required]
        public string UserId
        { get; set; } = null!;
        [Required]
        public string PostId

        { get; set; } = null!;

        public string? Content

        { get; set; }
        [Required]
        public DateTime TimeShare

        { get; set; } 

    }
}
