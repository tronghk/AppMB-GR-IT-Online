using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class PostSellProduct
    {
        [Required]
        [Key]
        public string PostSellProductId
        { get; set; } = null!;

        [Required]
        public string UserId
        { get; set; } = null!;
        public string ProductName { get; set; }

        [Required]
        public DateTime PostTime {  get; set; }
        public string Content
        { get; set; } = null!;

        [Required]
        public float Price
        { get; set; }
    }
}
