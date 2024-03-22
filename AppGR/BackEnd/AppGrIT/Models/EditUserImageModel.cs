using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class EditUserImageModel
    {
        [Required]
        public string UserId
        { get; set; } = null!;

        [Required]
        public string ImagePath
        { get; set; } = null!;


    }
}
