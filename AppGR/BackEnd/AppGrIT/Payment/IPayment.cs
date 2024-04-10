using AppGrIT.Model;
using AppGrIT.Models;
using Newtonsoft.Json.Linq;

namespace AppGrIT.Payment
{
    public interface IPayment
    {
        public string CreatePayment(string userId, string month);

        public Task<ResponseModel> SavePayment(ResultPaymentModel resultPaymentModel, string userId);

        public Task <ResponseModel> UpdatePayment(ResultPaymentModel resultPaymentModel, string userId);
    }
}
