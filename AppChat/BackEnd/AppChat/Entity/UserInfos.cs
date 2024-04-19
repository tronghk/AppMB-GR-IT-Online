using System.ComponentModel.DataAnnotations;

namespace AppChat.Entity
{
    public class UserInfos
    {
        [Required]
        public string Firstname
        { get; set; } = null!;
        [Required]
        public string LastName { get; set; } = null!;
        [Required]
        public string Gender { get; set; } = null!;
        [Required]
        public DateTime Birthday
        { get; set; }

        public string Address
        { get; set; } = null!;
        [Required]
        [Key]
        public string UserId
        { get; set; } = null!;

        public string Phone
        { get; set; } = null!;
    }
}
