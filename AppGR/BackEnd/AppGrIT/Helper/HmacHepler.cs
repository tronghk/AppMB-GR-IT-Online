using System.Security.Cryptography;
using System.Text;

namespace AppGrIT.Helper
{
    public class HmacHepler
    {
        public static String HmacSHA256(string inputData, string key)
        {
            byte[] keyByte = Encoding.UTF8.GetBytes(key);
            byte[] messageBytes = Encoding.UTF8.GetBytes(inputData);
            using (var hmac256 = new HMACSHA256(keyByte))
            {
                byte[] hmacmessage = hmac256.ComputeHash(messageBytes);
                string hex = BitConverter.ToString(hmacmessage);
                hex = hex.Replace("-", "").ToLower();
                return hex;
            }
        }
    }
}
