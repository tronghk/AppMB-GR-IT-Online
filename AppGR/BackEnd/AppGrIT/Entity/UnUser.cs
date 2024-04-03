using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class UnUser
    {
        [Key]
        [Required]
        public string UserId
        { get; set; } = null!;
        [Key]
        [Required]
        public string UnUserId
        { get; set; } = null!;

    }
}
