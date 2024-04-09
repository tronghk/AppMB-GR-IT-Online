using AppGrIT.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PostSellProductController : ControllerBase
    {
        private IPostSellProduct _postsellManager;
        public PostSellProductController(IPostSellProduct product)
        {
            _postsellManager = product;
            
        }
        [HttpGet("/FindProductByNameProduct")]
        public async Task<IActionResult> FindProductByNameProduct(string NameProduct)
        {
            var user = await _postsellManager.FindProductByNameProduct(NameProduct!);

            if (user != null)
            {
                var result = await _postsellManager.FindProductByNameProduct(NameProduct);
                return Ok(result);
            }
            return NotFound();
        }
    }
}
