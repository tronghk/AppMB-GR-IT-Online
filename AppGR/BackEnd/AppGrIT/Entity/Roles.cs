using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class Roles
    {
        [Required]
        public string IdRole { get; set; } = null!;
        [Required]
        public string RoleName { get; set; } = null!;
        [Required]
        public string RoleDescription { get; set; } = null!;
    }
}
