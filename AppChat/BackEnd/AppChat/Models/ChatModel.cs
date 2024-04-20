using System.ComponentModel.DataAnnotations;

namespace AppChat.Models
{
    public class ChatModel
    {
        [Required]
        public string UserId { get; set; }

        [Required]
        public string UserOrtherId
        { get; set; }

        public string? MessId
        { get; set; }
    }
}
