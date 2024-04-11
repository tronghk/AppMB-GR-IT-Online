
using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using MoMo;
using Newtonsoft.Json.Linq;

namespace AppGrIT.Payment
{
    public class PaymentServices : IPayment
    {
        private readonly IConfiguration _configuration;
        private readonly PaymentDAO _paymentDao;

        public PaymentServices(IConfiguration configuration, PaymentDAO payment)
        {
            _configuration = configuration;
            _paymentDao = payment;
        }

        public string CreatePayment(string userId, string month)
        {
            //request params need to request to MoMo system
            string endpoint = _configuration["Momo:endpoint"]!;
            string partnerCode = _configuration["Momo:partnerCode"]!;
            string accessKey = _configuration["Momo:accessKey"]!;
            string serectkey = _configuration["Momo:serectkey"]!;
            string orderInfo = _configuration["Momo:orderInfo"]!;
            string amount = (int.Parse(month) * 30000) + "";
            string returnUrl = _configuration["Momo:returnUrl"]!;
            string notifyurl = _configuration["Momo:notifyurl"]!; //lưu ý: notifyurl không được sử dụng localhost, có thể sử dụng ngrok để public localhost trong quá trình test


            string orderid = DateTime.Now.Ticks.ToString(); //mã đơn hàng
            string requestId = DateTime.Now.Ticks.ToString();
            string extraData = userId;

            //Before sign HMAC SHA256 signature
            string rawHash = "partnerCode=" +
                partnerCode + "&accessKey=" +
                accessKey + "&requestId=" +
                requestId + "&amount=" +
                amount + "&orderId=" +
                orderid + "&orderInfo=" +
                orderInfo + "&returnUrl=" +
                returnUrl + "&notifyUrl=" +
                notifyurl + "&extraData=" +
                extraData;

            MoMoSecurity crypto = new MoMoSecurity();
            //sign signature SHA256
            string signature = crypto.signSHA256(rawHash, serectkey);

            //build body json request
            JObject message = new JObject
            {
                { "partnerCode", partnerCode },
                { "accessKey", accessKey },
                { "requestId", requestId },
                { "amount", amount },
                { "orderId", orderid },
                { "orderInfo", orderInfo },
                { "returnUrl", returnUrl },
                { "notifyUrl", notifyurl },
                { "extraData", extraData },
                { "requestType", "captureMoMoWallet" },
                { "signature", signature }
                

            };

            string responseFromMomo = PaymentRequest.sendPaymentRequest(endpoint, message.ToString());

            JObject jmessage = JObject.Parse(responseFromMomo);
            return jmessage.GetValue("payUrl")!.ToString();

        }

        public async Task<ResponseModel> SavePayment(ResultPaymentModel resultPaymentModel, string userId)
        {
            Bills b = new Bills
            {
                orderId = resultPaymentModel.orderId,
                orderTime = resultPaymentModel.responseTime,
                userId = userId,
                money = resultPaymentModel.amount

            };
            var result = await _paymentDao.CreateBill(b);

            if(result.Equals(StatusResponse.STATUS_SUCCESS))
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,

                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = result
            };
        }

        public async Task<ResponseModel> UpdatePayment(ResultPaymentModel resultPaymentModel, string userId)
        {
            int month = (int.Parse(resultPaymentModel.amount) / 30000);
            var userSell = await _paymentDao.GetUserSell(userId);

            if(userSell == null)
            {
                UserSells us = new UserSells
                {
                    UserId = userId,
                    TimeStart = DateTime.Now,
                    TimeEnd = DateTime.Now.AddMonths(month)
                    , MoneyUp = float.Parse(resultPaymentModel.amount)
                };
                await _paymentDao.CreateUserSell(us);
            }
            else
            {
                userSell.TimeEnd = DateTime.Now.AddMonths(month);
                userSell.MoneyUp = userSell.MoneyUp + float.Parse(resultPaymentModel.amount);
                await _paymentDao.EditUserSellAsync(userSell);
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_SUCCESS,
                Message = MessageResponse.MESSAGE_UPDATE_SUCCESS,
            };
        }
    }
}
