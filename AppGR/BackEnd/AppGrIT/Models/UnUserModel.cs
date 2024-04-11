using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class UnUserModel
    {
        [Required]
        public string UserId
        { get; set; } = null!;
        [Required]
        public string UnUserId
        { get; set; } = null!;
        
    }
}
