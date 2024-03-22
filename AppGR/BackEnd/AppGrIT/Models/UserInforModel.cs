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
        [Required]
        public string Gender
        { get; set; } = null!;
        public DateTime Birthday
        { get; set; }
        public string Address
        { get; set; } = null!;
        public string UserId
        { get; set; } = null!;
        public string Phone
        { get; set; } = null!;
    }
}
