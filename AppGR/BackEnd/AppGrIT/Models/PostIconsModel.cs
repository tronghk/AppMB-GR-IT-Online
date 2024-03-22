using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class PostIconsModel
    {

        [Required]
        public string UserId { get; set; } = null!;
        [Required]
        public int Expression
        { get; set; }
        [Required]
        public string UserName
        { get; set; } = null!;
        [Required]
        public string UserImage
        { get; set; } = null!;
    }
}
