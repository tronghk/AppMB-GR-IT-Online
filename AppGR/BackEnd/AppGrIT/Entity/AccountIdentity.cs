using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class AccountIdentity
    {
        [Required]
        [EmailAddress]
        public string Email { get; set; } = null!;

        [Required]
        public bool EmailComfirm = false;

        [Required]
        public int countLocked = 0;
        [Required]
        public bool Locked = false;

        [Required]
        public DateTime TimeLocked;
        public string? RefreshToken { get; set; }
        public DateTime RefreshTokenExpiryTime { get; set; }

        [Required]
        public string UserId { get; set; } = null!;
    }
}
