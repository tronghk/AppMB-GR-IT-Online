using System.ComponentModel.DataAnnotations;

namespace AppChat.Models
{
    public class GroupMemberModel
    {
        public string? GroupId { get; set; }
        [Required]
        public string UserId
        { get; set; }
        [Required]
        public string Role
            { get; set; }
        }
}
