using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class PostSellProductModel
    {
        [Required]
        public string PostSellProductId
        { get; set; } = null!;
        [Required]
        public string UserId
        { get; set; } = null!;
        public string Content

        { get; set; } = null!;
        [Required]
        public float Price

        { get; set; }
    }
}
