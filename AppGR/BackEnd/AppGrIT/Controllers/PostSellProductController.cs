using AppGrIT.Helper;
using AppGrIT.Services;
using Microsoft.AspNetCore.Authorization;
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
            var result = await _postsellManager.FindProductByNameProduct(NameProduct);

            if (result.Count > 0)
            {
                
                return Ok(result);
            }
            return NotFound();
        }
        [HttpGet("/FindProductByPriceProduct")]
        public async Task<IActionResult> FindProductByPriceProduct(float price)
        {
            var result = await _postsellManager.FindProductByPriceProduct(price);

            if (result.Count > 0)
            {
               
                return Ok(result);
            }
            return NotFound();
        }
   
        [HttpGet("/post-sell-product")]
        public async Task<IActionResult> GetPostSellProduct()
        {
            var result = await _postsellManager.GetPostSellProductModel();

            if (result.Count > 0)
            {

                return Ok(result);
            }
            return NotFound();
        }
    }
}
