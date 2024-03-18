using AppGrIT.Models;
using BookManager.Model;
using Microsoft.AspNetCore.Identity;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

namespace AppGrIT.Authentication
{
    public class TokenServices :IToken
    {
        private IConfiguration _configuration;

        public TokenServices(
            IConfiguration configuration) {
            this._configuration = configuration;
        }
        public async Task<TokenModel> GenerareTokenModel(SignInModel model)
        {
            var user = model.Email;
            var claims = new List<Claim>
            {
                new Claim(ClaimTypes.Email,model.Email),
                new Claim(ClaimTypes.Name,model.Email),
                new Claim(Microsoft.IdentityModel.JsonWebTokens.JwtRegisteredClaimNames.Jti,Guid.NewGuid().ToString())
            };

            //get role
            var userRole = new List<string>();

            foreach (var role in userRole)
            {
                claims.Add(new Claim(ClaimTypes.Role, role.ToString()));
            }

            var token = GeneratorToken(claims);
            var refreshToken = GenerateRefreshToken();

            _ = int.TryParse(_configuration["JWT:RefreshTokenValidityInDays"], out int refreshTokenValidityInDays);

            // cập nhật refreshtoken vào user
            /* user!.RefreshToken = refreshToken;
             user.RefreshTokenExpiryTime = DateTime.Now.AddDays(refreshTokenValidityInDays);
             await _userManager.UpdateAsync(user);*/
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

            var authSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["JWT:Secret"]));
            _ = int.TryParse(_configuration["JWT:TokenValidityInMinutes"], out int tokenValidityInMinutes);

            var token = new JwtSecurityToken(
                issuer: _configuration["JWT:ValidIssuer"],
                audience: _configuration["JWT:ValidAudience"],
                expires: DateTime.Now.AddMinutes(tokenValidityInMinutes),
                claims: authClaims,
                signingCredentials: new SigningCredentials(authSigningKey, SecurityAlgorithms.HmacSha256)
                );

            return token;
        }
    }
}
