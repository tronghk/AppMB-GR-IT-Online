using FireSharp.Config;
using FireSharp.Interfaces;

namespace AppGrIT.Data
{
    public class ConnectFirebase
    {
        private readonly IConfiguration _configuration;
        private readonly IFirebaseConfig _config;
        public readonly IFirebaseClient _client;
        public ConnectFirebase(IConfiguration configuration)
        {
           
            _configuration = configuration;
            _config = new FirebaseConfig
            {
                AuthSecret = _configuration["Firebase:Secret"],
                BasePath = _configuration["Firebase:DbAddress"]
            };
            _client = new FireSharp.FirebaseClient(_config);
        }
        
    }
}
