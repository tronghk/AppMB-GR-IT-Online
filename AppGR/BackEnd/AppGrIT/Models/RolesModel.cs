using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class RolesModel
    {
        [Required]
        public string RoleName { get; set; } = null!;
        [Required]
        public string Despripsion { get; set; } = null!;



    }
}
