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
           if(jsonResponse != null )
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();

                    var user = JsonConvert.DeserializeObject<PostSellProduct>(value);

                    if (user.ProductName != null && user.ProductName.ToLower().Contains(nameProduct.ToLower()))
                    {
                        matchingUsers.Add(user);
                    }
                }
            }
            return matchingUsers;
        }
        public async Task<List<PostSellProduct>> FindProductByPriceAsync(float price)
        {
            List<PostSellProduct> matchingProducts = new List<PostSellProduct>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostSellProduct");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    var product = JsonConvert.DeserializeObject<PostSellProduct>(value);
                    if (product.Price != null && product.Price.Equals(price))
                    {


                        matchingProducts.Add(product);

                    }
                  
                }
               
            }
            return matchingProducts;
        }
        public async Task<string> SumPayment(int month)
        {
            float sum = 0;
            FirebaseResponse response = await _firebase._client.GetAsync("UserSells");
            //path All Id
            JObject jsonResponse = response.ResultAs<JObject>();
            if (jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    //path Object
                    var us = JsonConvert.DeserializeObject<UserSells>(value);
                    var timeStart = us.TimeStart;
                    var timeEnd = us.TimeEnd;

                    if (timeEnd.Year == timeEnd.Year)
                    {
                        sum = (timeEnd.Month - timeStart.Month) * 30000;
                    }
                    else
                    {
                        sum = (12 - timeStart.Month) * 30000 + timeEnd.Month * 30000;
                    }
                }



            }
            return sum.ToString();

        }
        public async Task<List<PostSellProduct>> GetPostSellProduct()
        {
            List<PostSellProduct> matchingProducts = new List<PostSellProduct>();
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostSellProduct");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
           if(jsonResponse != null)
            {
                foreach (var item in jsonResponse)
                {
                    var value = item.Value!.ToString();
                    var product = JsonConvert.DeserializeObject<PostSellProduct>(value);
                    matchingProducts.Add(product);
                }
               
            }
            return matchingProducts;
        }

    }
}
