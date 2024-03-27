using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using AppGrIT.Services;
using BookManager.Model;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

namespace AppGrIT.Authentication
{
    public class TokenServices : IToken
    {
        private IConfiguration _configuration;
        private readonly IUsers _userManager;
        private readonly IRoles _roleManager;

        public TokenServices(
            IConfiguration configuration, IUsers user, IRoles role)
        {
            this._configuration = configuration;
            _userManager = user;
            _roleManager = role;
        }
        public async Task<TokenModel> GenerareTokenModel(SignInModel model)
        {
            var user = await _userManager.GetUserAsync(model.Email);
            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.Email,model.Email),
                new Claim(ClaimTypes.Name,model.Email),
                new Claim(Microsoft.IdentityModel.JsonWebTokens.JwtRegisteredClaimNames.Jti,Guid.NewGuid().ToString())
            };

            //get role
            if (user != null)
            {
                var userRoles = await _roleManager.GetUserRoles(user.UserId);
                foreach (var role in userRoles)
                {
                    claims.Add(new Claim(ClaimTypes.Role, role.ToString()));
                }
            }
            var token = GeneratorToken(claims);
            var refreshToken = GenerateRefreshToken();

            _ = int.TryParse(_configuration["JWT:RefreshTokenValidityInDays"], out int refreshTokenValidityInDays);

            // cập nhật refreshtoken vào user

            DateTime RefreshTokenExpiryTime = DateTime.UtcNow.AddDays(refreshTokenValidityInDays);

            await _userManager.UpdateRefeshTokenAccountAsync(model.Email, refreshToken, RefreshTokenExpiryTime);

            var result = new TokenModel
            {
                AccessToken = new JwtSecurityTokenHandler().WriteToken(token),
                RefreshToken = refreshToken,
                Expiration = token.ValidTo
            };

            return result;
        }

        private static string GenerateRefreshToken()
        {
            var randomNumber = new byte[64];
            using var rng = RandomNumberGenerator.Create();
            rng.GetBytes(randomNumber);
            return Convert.ToBase64String(randomNumber);
        }
        public JwtSecurityToken GeneratorToken(List<Claim> authClaims)
        {

            var authSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]!));
            int date = int.Parse(_configuration["JWT:TokenValidityInMinutes"]!);
            
            var token = new JwtSecurityToken(
                issuer: _configuration["JWT:ValidIssuer"],
                audience: _configuration["JWT:ValidAudience"],
                expires: DateTime.UtcNow.AddMinutes(date),
                claims: authClaims,
                signingCredentials: new SigningCredentials(authSigningKey, SecurityAlgorithms.HmacSha256)
                );

            return token;
        }

        public async Task<TokenModel> RefreshToken(List<Claim> authClaims, string email)
        {
            var user = await _userManager.GetUserAsync(email);
            var token = GeneratorToken(authClaims);
            var refreshToken = GenerateRefreshToken();
            user!.RefreshToken = refreshToken;
            await _userManager.UpdateRefeshTokenAccountAsync(email, refreshToken, user.RefreshTokenExpiryTime);

            var result = new TokenModel
            {
                AccessToken = new JwtSecurityTokenHandler().WriteToken(token),
                RefreshToken = refreshToken,
                Expiration = token.ValidTo
            };

            return result;
        }
        public async Task<ResponseModel> CheckToken(TokenModel tokenModel)
        {
            //check token còn hạn
            if (tokenModel.Expiration >= DateTime.UtcNow)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "The token is still valid"
                };
            }
            // check email không tìm thấy

            if (tokenModel is null)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "token is null"
                };
            }
            string? accessToken = tokenModel.AccessToken;
            string? refreshToken = tokenModel.RefreshToken;
            var principal = GetPrincipalFromExpiredToken(accessToken);
            if (principal == null)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Invalid access token or refresh token"
                };

            }
            string username = principal.Identity!.Name!;
            var user = await _userManager.GetUserAsync(username);

            if (user == null || user.RefreshToken != refreshToken || user.RefreshTokenExpiryTime <= DateTime.Now)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Invalid access token or refresh token"
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_SUCCESS,
                Message = MessageResponse.MESSAGE_CREATE_SUCCESS
            };
        }
        public ClaimsPrincipal? GetPrincipalFromExpiredToken(string? token)
        {
            var tokenValidationParameters = new TokenValidationParameters
            {
                ValidateAudience = false,
                ValidateIssuer = false,
                ValidateIssuerSigningKey = true,
                IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]!)),
                ValidateLifetime = false
            };

            var tokenHandler = new JwtSecurityTokenHandler();
            var principal = tokenHandler.ValidateToken(token, tokenValidationParameters, out SecurityToken securityToken);
            if (securityToken is not JwtSecurityToken jwtSecurityToken || !jwtSecurityToken.Header.Alg.Equals(SecurityAlgorithms.HmacSha256, StringComparison.InvariantCultureIgnoreCase))
                throw new SecurityTokenException("Invalid token");

            return principal;

        }

        public bool CheckDupEmailToToken(string? token, string email)
        {
            var pri = GetPrincipalFromExpiredToken(token);
            ;
            if (email.Equals(pri.Identity!.Name))
                return true;
            return false;
        }
    }
}
