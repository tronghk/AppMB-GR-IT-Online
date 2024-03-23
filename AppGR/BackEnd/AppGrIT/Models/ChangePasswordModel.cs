using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class ChangePasswordModel
    {
        [Required]
        [EmailAddress]
        public string Email { get; set; } = null!;
        
        [Required]
        public string OldPassword
        { get; set; } = null!;
        [MinLength(6)]
        [MaxLength(15)]
        [Required]
        public string NewPassword
        { get; set; } = null!;
    }
}
