using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class ExpressionModel
    {
        [Required]
        public string PostId { get; set; } = null!;
        [Required]
        public string UserId
        { get; set; } = null!;
        [Required]
        public int Expression
        { get; set; }

        [Required]
        public string Type
        { get; set; } = null!;
    }
}
