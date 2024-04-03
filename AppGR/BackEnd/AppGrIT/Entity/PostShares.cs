using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class PostShares
    {
        [Required]
        public string PostId
        { get; set; } = null!;
        [Required]
        public string UserId

        { get; set; } = null!;
        public string ContentShare
        { get; set; } = null!;
        [Required]
        public DateTime TimeShare
        { get; set; }
    }
}
