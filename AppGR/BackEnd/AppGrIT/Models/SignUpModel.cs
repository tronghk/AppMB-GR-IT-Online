using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Model
{
    public class SignUpModel
    {
        [Required(ErrorMessage = "Email is required")]
        [EmailAddress]
        public string Email { get; set; } = null!;
        [Required(ErrorMessage = "Password is required")]
        public string Password { get; set; } = null!;
        [Required]
        public string FirstName { get; set; } = null!;
        [Required] public string LastName { get; set; } = null!;

        [Required] public DateTime Birthday { get; set; }

        [Required] public string Gender { get; set; } = null!;
        [Required]
        public string PasswordConfirmation { get; set; } =null!;
    }
}
