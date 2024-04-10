using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Models;
using Firebase.Auth;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Configuration;
using System.Net;

namespace AppGrIT.Services.Imployement
{
    public class PostSellProductServices : IPostSellProduct
    {
        private readonly PostSellProductDAO _postSellDao;
        public PostSellProductServices(PostSellProductDAO postSell)
        {
            _postSellDao = postSell;
        }
        public async Task<List<PostSellProductModel>> FindProductByNameProduct(string nameProduct)
        {
            List<PostSellProductModel> result = new List<PostSellProductModel>();
            var list = await _postSellDao.FindProductBySubstringContentAsync(nameProduct);
            foreach (var userinfo in list)
            {
                var us = new PostSellProductModel
                {
                    UserId = userinfo.UserId,
                    ProductName = userinfo.ProductName,
                    Price = userinfo.Price,
                    PostSellProductId = userinfo.PostSellProductId,
                    Content = userinfo.Content,
                   
                    PostTime = userinfo.PostTime 
                };
                result.Add(us);
            }
            return result;
        }

        public async Task<List<PostSellProductModel>> FindProductByPriceProduct(float price)
        {
            List<PostSellProductModel> result = new List<PostSellProductModel>();
            var list = await _postSellDao.FindProductByPriceAsync(price);
            foreach (var userinfo in list)
            {
                var us = new PostSellProductModel
                {
                    UserId = userinfo.UserId,
                    ProductName = userinfo.ProductName,
                    Price = userinfo.Price,
                    PostSellProductId = userinfo.PostSellProductId,
                    Content = userinfo.Content,                  
                    PostTime = userinfo.PostTime 
                };
                result.Add(us);
            }
            return result;
        }
    }
}
