using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class DeletePostModel
    {
        [Required]
        public string PostId { get; set; } = null!;
        [Required]
        public string UserId
        { get; set;} = null!;


    }
}
