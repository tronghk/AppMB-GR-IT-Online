using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Models
{
    public class PostModel
    {
        public string? PostId
        { get; set; }
        public string? UserId
        { get; set; }

        public DateTime PostTime { get; set; }



        public string? Content
        { get; set; }
        public string? PostType { get; set; }
    }
}
