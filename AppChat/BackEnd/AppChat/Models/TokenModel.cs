using System.ComponentModel.DataAnnotations;

namespace BookManager.Model
{
    public class TokenModel
    {
        [Required]
        public string? AccessToken { get; set; }
        [Required]
        public string? RefreshToken { get; set; }
        [Required]
        public DateTime? Expiration { get; set;}
    }
}
