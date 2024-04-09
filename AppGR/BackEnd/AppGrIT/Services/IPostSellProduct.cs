using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IPostSellProduct
    {
        public Task<List<PostSellProductModel>> FindProductByNameProduct(string nameProduct);
    }
}
