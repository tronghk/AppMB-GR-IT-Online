using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class UserFollowsModel
    {
        [Required]
        public string UserId
        { get; set; } = null!;
        [Required]
        public string UserFollowId { get; set; } = null!;
    }
}
