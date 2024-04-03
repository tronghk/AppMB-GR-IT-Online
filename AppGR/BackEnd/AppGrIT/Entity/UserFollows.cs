using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class UserFollows
    {
        [Required]
        [Key]
        public string UserId
        { get; set; } = null!;
        [Required]
        [Key]
        public string UserFollowId
        { get; set; } = null!;
    }
}
