using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class SignInModel
    {
        [Required]
        [EmailAddress]
        public string Email { get; set; } = null!;

        [Required]
        [MinLength(6)]
        public string Password { get; set; } = null!;
    }
}
