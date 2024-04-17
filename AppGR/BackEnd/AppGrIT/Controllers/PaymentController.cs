using AppGrIT.Helper;
using AppGrIT.Models;
using AppGrIT.Payment;
using AppGrIT.Services;
using FirebaseAdmin.Messaging;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using MoMo;
using Newtonsoft.Json.Linq;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class PaymentController : ControllerBase
    {
        private readonly IPayment _paymentManager;
        private readonly IRoles _roleManager;
        private readonly IUsers _userManager;

        public PaymentController(IPayment payment, IRoles role, IUsers user) {
        
            _paymentManager = payment;
            _roleManager = role;
            _userManager = user;
        }

        [HttpPost("/payment-user")]
        public async Task<IActionResult> Payment(string month, string userId)
        {
           var json = _paymentManager.CreatePayment(userId, month);
           return Ok(json);
        }
        [HttpGet("/ConfirmPaymentClient")]
        public async Task<IActionResult> ConfirmPaymentClient(string partnerCode, string accessKey
            ,string requestId, string amount, string orderId, string orderInfo, string orderType, string transId
            , string message, string localMessage, string responseTime, string errorCode, string payType, string extraData
            , string signature)
        {
            // so sánh accesskey partnercode....
            // luu db
            if(errorCode == "0")
            {
                var model = new ResultPaymentModel
                {
                    partnerCode = partnerCode,
                    accessKey = accessKey,
                    requestId = requestId,
                    amount = amount,
                    orderId = orderId,
                    orderType = orderType,
                    responseTime = responseTime,
                    errorCode = errorCode,
                    payType = payType,
                    extraData = extraData,
                    signature = signature,
                    localMessage = localMessage,
                    orderInfo = orderInfo,
                    transId = transId
                    ,message = message
                };
                //luu db
                var result = await _paymentManager.SavePayment(model, extraData);

                //update quyen neu có
                var user = await _userManager.GetUserToUserId(extraData);
                var role = await _roleManager.AddUserRolesAsync(
                    new UserRoleModel
                    {
                        RoleName = SynthesizeRoles.SELL_PRODUCT,
                        Email = user.Email
                    });
                // update thời hạn
                var time = await _paymentManager.UpdatePayment(model, extraData);

                if(result.Status == StatusResponse.STATUS_SUCCESS &&
                    role.Status == StatusResponse.STATUS_SUCCESS &&
                    time.Status == StatusResponse.STATUS_SUCCESS 
                    )   
                        return Ok("Đã đăng ký thành viên bán hàng thành công vui lòng đăng nhập lại");
            }
           
            
                return BadRequest("Đăng ký bán hàng thất bại");
            
        }


        [HttpPost("/SavePayment")]
        public void SavePayment()
        {
            string abc = "";
        }
    }
}
