
using AppGrIT.Authentication;
using AppGrIT.Data;
using AppGrIT.Model;
using AppGrIT.Models;
using AppGrIT.Services;
using Firebase.Auth;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using System.IO;

namespace AppGrIT.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {

        private readonly IUsers _userManager;
        private readonly IToken _tokenManager;
        public UsersController(IUsers userManager, IToken tokenManager)
        {
            _tokenManager = tokenManager;
            _userManager = userManager;
        }

        [HttpPost("/signup")]
        public async Task<IActionResult> Signup(SignUpModel model)
        {
           
            var result = await _userManager.SignUpAsync(model);
            if(!result.Status!.Equals("Ok"))
            {
                return BadRequest(result);
            }
            return Ok(result);

           
        }
        [HttpPost("/signin")]
        public async Task<IActionResult> Login(SignInModel signInModel)
        {
           var result = await _userManager.SignInAsync(signInModel);
            if (result.Status.Equals("Ok"))
            {
                var token = await _tokenManager.GenerareTokenModel(signInModel);
                return Ok(token);
            }
            else
            {
                return BadRequest("fail");
            }
        }
        [Authorize]
        [HttpGet("/getall")]
        public async Task<IActionResult> getall()
        {
            return Ok("abc");
        }
    }
}
