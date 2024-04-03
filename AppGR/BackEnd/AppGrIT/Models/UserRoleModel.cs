using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class UserRoleModel
    {
        [Required]
        public string RoleName { get; set; }
        [Required]
        public string Email { get; set; }
    }
}
