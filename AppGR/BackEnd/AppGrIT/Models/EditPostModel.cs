using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class EditPostModel
    {
        [Required]
        public string PostId { get; set; } = null!;
        [Required]
        public string UserId
        { get; set; } = null!;

        public string Content
        { get; set; } = null!;
        [Required]
        public DateTime EditTime
        { get; set; }

        public List <ImagePostModel> ImagePosts { get; set; } = new List<ImagePostModel>();

    }
}
