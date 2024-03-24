using AppGrIT.Entity;
using FireSharp.Response;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Security.Principal;

namespace AppGrIT.Data
{
    public class ExpressionDAO
    {
        private readonly ConnectFirebase _firebase;

        public ExpressionDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<int> CountExpressionInPost(string postId, string expression)
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("PostExpressions");
            JObject jsonResponse = firebaseResponse.ResultAs<JObject>();
            if(jsonResponse == null)
            {
                return 0;
            }
            int count = 0;
            foreach (var item in jsonResponse)
            {
                var value = item.Value!.ToString();
                //path Object
               var  postExpression = JsonConvert.DeserializeObject<PostExpressions>(value);
               if(postExpression.PostId.Equals(postId) && postExpression.Expression == int.Parse(expression)) {
                    count++;
                }
            }
            return count;
        }
    }
}
