using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class UserFriends
    {
        [Required]
        [Key]
        public string UserId
        { get; set; } = null!;

        [Key]
        [Required]
        public string UserFriendId
        { get; set; } = null!;

        [Required]
        public string FriendShipTime
        { get; set; } = null!;
        public string Status
        { get; set; }
    }
}
