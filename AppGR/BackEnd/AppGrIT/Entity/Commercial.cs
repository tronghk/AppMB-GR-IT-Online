using System.ComponentModel.DataAnnotations;

namespace AppGrIT.Entity
{
    public class Commercial
    {
        [Required][Key] public string CommercialId { get; set; } = null!;
        [Required] public string UserId
        { get; set; } = null!;
        [Required]
        public string Content

        { get; set; } = null!;
        [Required]
        public DateTime TimeStart

        { get; set; }
        [Required]
        public DateTime TimeEnd

        { get; set; }
        public string VideoPath
        { get; set; } = null!;
    }
}
