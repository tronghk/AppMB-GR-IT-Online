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
<<<<<<< HEAD
        public string Address
        { get; set; } = null!;
=======
        public string? Address
        { get; set; }
>>>>>>> 95afc96f360d93dc98d2ae9040ca728de2d4e3ea
        public string UserId
        { get; set; } = null!;
        public string? Phone
        { get; set; }
    }
}
