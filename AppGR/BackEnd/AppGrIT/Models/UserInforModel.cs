using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class UserInforModel
    {
        [Required]
        public string Firstname { get; set; } = null!;
        [Required]
        public string LastName
        { get; set; } = null!;
        public string? Gender
        { get; set; }
        public DateTime Birthday
        { get; set; }
        public string? Address
        { get; set; }
        [Required]
        public string UserId
        { get; set; } = null!;
        public string? Phone
        { get; set; }
    }
}
