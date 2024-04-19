using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Models;
using Firebase.Auth;
using Google.Api.Gax.Rest;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Configuration;
using System.Net;

namespace AppGrIT.Services.Imployement
{
    public class PostSellProductServices : IPostSellProduct
    {
        private readonly PostSellProductDAO _postSellDao;
        private readonly IImages _imageManager;

        public PostSellProductServices(PostSellProductDAO postSell, IImages imageManager)
        {
            _postSellDao = postSell;
            _imageManager = imageManager;
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
                var listImage = await _imageManager.GetImagePostToId(us.PostSellProductId);
                us.imagePosts = listImage;

                result.Add(us);
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
                var listImage = await _imageManager.GetImagePostToId(us.PostSellProductId);
                us.imagePosts = listImage;

                result.Add(us);
                result.Add(us);
            }
            return result;
        }

        public async Task<List<PostSellProductModel>> GetPostSellProductModel()
        {
            List<PostSellProductModel> result = new List<PostSellProductModel>();
            var list = await _postSellDao.GetPostSellProduct();
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
                var listImage = await _imageManager.GetImagePostToId(us.PostSellProductId);
                us.imagePosts = listImage;
               
                result.Add(us);
            }
            return result;
        }
    }
}
