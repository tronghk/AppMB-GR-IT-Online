using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class PostSellProductModel
    {
        public string PostSellProductId
        { get; set; }
        [Required]
        public string UserId
        { get; set; } = null!;
        public string? Content

        { get; set; }
        public string ProductName { get; set; }
        [Required]
        public List<ImagePostModel>? imagePosts { get; set; }
        [Required]
        public DateTime PostTime { get; set; }
        [Required]
        public float Price

        { get; set; }
    }
}
