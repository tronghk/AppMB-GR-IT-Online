using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using AppGrIT.Services;
using Microsoft.AspNetCore.Authorization;
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
            var result = await _imageManager.AddImagesPostAsync(list);
            if(result.Count > 0)
            {
                return Ok(result);
            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            });
           
        }

        [HttpPost("/add-list-image-from-post")]
        public async Task<IActionResult> AddListImagePost(List<IFormFile> image)
        {
           
           
            var result = await _imageManager.AddImagesPostAsync(image);
            if (result.Count > 0)
            {
                return Ok(result);
            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            });

        }
        [HttpGet("/get-img-avatar-default")]
        public async Task<IActionResult> GetAvatarDefault()
        {
            var result = await _imageManager.GetLinkAvatarDefault();
            return Ok(result);

        }
        [HttpGet("/get-img-cover-default")]
        public async Task<IActionResult> GetCoverDefault()
        {
            var result = await _imageManager.GetLinkCoverDefault();
            return Ok(result);

        }
    }
}
