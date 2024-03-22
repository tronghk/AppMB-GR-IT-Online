using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class UserRenewalModel
    {
        [Required]
        public string UserId
        { get; set; } = null!;

        [Required]
        public float Price
        { get; set; }
    }
}
