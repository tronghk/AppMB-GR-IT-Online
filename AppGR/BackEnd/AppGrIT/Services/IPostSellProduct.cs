using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IPostSellProduct
    {
        public Task<List<PostSellProductModel>> FindProductByNameProduct(string nameProduct);
        public Task<List<PostSellProductModel>> FindProductByPriceProduct(float price);

        public Task<List<PostSellProductModel>> GetPostSellProductModel();


    }
}
