using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using Firebase.Auth;

namespace AppGrIT.Data
{
    public class PostSellProductDAO
    {
        private readonly ConnectFirebase _firebase;
        public PostSellProductDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<List<PostSellProduct>> FindProductBySubstringContentAsync(string nameProduct)
        {
            List<PostSellProduct> matchingUsers = new List<PostSellProduct>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostSellProduct");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            foreach (var item in jsonResponse)
            {
                var value = item.Value!.ToString();

                var user = JsonConvert.DeserializeObject<PostSellProduct>(value);

                if (user.NameProduct != null && user.NameProduct.ToLower().Contains(nameProduct.ToLower()))
                {
                    matchingUsers.Add(user);
                }
            }
            return matchingUsers;
        }
        public async Task<List<PostSellProduct>> FindProductByPriceAsync(float price)
        {
            List<PostSellProduct> matchingProducts = new List<PostSellProduct>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostSellProduct");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            foreach (var item in jsonResponse)
            {
                var value = item.Value!.ToString();
                var product = JsonConvert.DeserializeObject<PostSellProduct>(value);               
                if (product.Price != null && product.Price.Equals(price))
                {
                    
                   
                        matchingProducts.Add(product);
                    
                }
            }
            return matchingProducts;
        }

    }
}
