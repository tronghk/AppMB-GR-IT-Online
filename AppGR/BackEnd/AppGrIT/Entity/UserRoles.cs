using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class UserRoles
    {
        [Required]
        public string UserId { get; set; } = null!;
        [Required]
        public string RoleId { get; set; } = null!;
    }
}
