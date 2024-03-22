using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class UserInfors
    {
        [Required]
        public string FirstName
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
