using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class UserSells
    {
        [Required]
        public string UserId
        { get; set; } = null!;
        [Required]
        public DateTime TimeStart
        { get; set; }

        [Required]
        public DateTime TimeEnd
        { get; set; }
    }
}
