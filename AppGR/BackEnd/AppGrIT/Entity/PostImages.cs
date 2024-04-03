using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class PostImages
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
