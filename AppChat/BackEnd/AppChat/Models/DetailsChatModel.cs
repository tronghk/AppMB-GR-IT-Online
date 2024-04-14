using System.ComponentModel.DataAnnotations;

namespace AppChat.Models
{
    public class DetailsChatModel
    {
        [Required]
        public string ChatId { get; set; }
        [Required]
        public string UserId
        { get; set; }
        [Required]
        public DateTime Time
        { get; set; }
        public string Content
        { get; set; }
        public string ImagePath
        { get; set; }
    }
}
