using System.ComponentModel.DataAnnotations;

namespace AppChat.Models
{
    public class ImagePostModel
    {
        public string? ImageContent
        { get; set; }

        [Required]
        public string? ImagePath
        { get; set; } = null!;

        public string? ImageId
        { get; set; }


    }
}
