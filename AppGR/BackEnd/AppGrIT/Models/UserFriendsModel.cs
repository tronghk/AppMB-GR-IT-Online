using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class UserFriendsModel
    {
        [Required]
        public string UserId
        { get; set; } = null!;
        [Required]
        public string UserFriendId 
        { get;  set; } = null!;
       
       
    }
}
