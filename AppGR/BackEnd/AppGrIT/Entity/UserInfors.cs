using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class UserInfors
    {
        [Required]
        public string Firstname
        { get; set; } = null!;
        [Required]
        public string Lastname { get; set; } = null!;
        [Required]
        public string Gender { get; set; } = null!;
        [Required]
        public string Birthday
        { get; set; } = null!;

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
