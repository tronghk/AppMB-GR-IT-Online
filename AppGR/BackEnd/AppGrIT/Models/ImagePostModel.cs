using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class ImagePostModel
    {
        [Required]
        public string ImageContent
        { get; set; } = null!;

        [Required]
        public string ImagePath
        { get; set; } = null!;

    }
}
