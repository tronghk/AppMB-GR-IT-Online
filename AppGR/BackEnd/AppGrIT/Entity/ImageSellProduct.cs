using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class ImageSellProduct
    {
        [Key]
        [Required]
        public string ImageSellProductId { get; set; } = null!;
        [Required]
        public string PostSellProductId
        { get; set; } = null!;
        [Required]
        public string ImagePath
        { get; set; } = null!;

        public string Content
        { get; set; } = null!;

    }
}
