using System.ComponentModel.DataAnnotations;

namespace AppChat.Entity
{
    public class PostImage
    {
        [Required]
        public string PostImageId { get; set; } = null!;

        [Required]
        public string PostId
        { get; set; } = null!;

        [Required]
        public string ImagePath
        { get; set; } = null!;

        public string ImageContent
        { get; set; } = null!;
    }
}
