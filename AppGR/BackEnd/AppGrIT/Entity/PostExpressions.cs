using Microsoft.AspNetCore.DataProtection.KeyManagement;
using Newtonsoft.Json;
using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class PostExpressions
    {
        [Required]
        [Key]
        public string PostId
        { get; set; } = null!;

        [Required]
        [Key]
        public string UserId
        { get; set; } = null!;

        [Required]
        public int Expression {  get; set; }

    }
}
