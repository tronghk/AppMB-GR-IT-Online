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
        [Required]
        public string NewPassword
        { get; set; } = null!;
    }
}
