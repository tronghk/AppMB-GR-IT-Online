using AppGrIT.Models;
using AppGrIT.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ImagesController : ControllerBase
    {
        private readonly IImages _imageManager;

        public ImagesController(IImages image) {
            _imageManager = image;
        }
        [HttpPost("/add-image-from-post")]
        public async Task<IActionResult> AddImagePost(IFormFile image)
        {
            var list = new List<IFormFile>();
            list.Add(image);
            var result = await _imageManager.AddImagesPostAsync(list[0],"aaa");
            return Ok(result);
        }
    }
}
