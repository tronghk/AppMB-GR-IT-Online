using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class UserFollowsModel
    {
        [Required]
        public string UserId
        { get; set; } = null!;
        [Required]
        public string UserName { get; set; } = null!;
        [Required]
        public string UserImage
        { get; set; } = null!;
    }
}
