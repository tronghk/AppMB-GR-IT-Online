using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class PostIconsModel
    {

        [Required]
        public string? UserId { get; set; }
        [Required]
        public int Expression
        { get; set; }
        [Required]
        public string? UserName
        { get; set; }
        [Required]
        public string? UserImage
        { get; set; }
    }
}
