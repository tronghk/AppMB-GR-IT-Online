using AppGrIT.Entity;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IRoles
    {
        public Task<ResponseModel> AddRoleAsync(RolesModel model);
        public Task<ResponseModel> DeleteRoleAsync(RolesModel model);
        public Task<ResponseModel> DeleteUserRolesAsync(UserRoleModel model);
        public Task<ResponseModel> AddUserRolesAsync(UserRoleModel model);

        public Task<Roles> GetRoleAsync(string roleName);
        public Task<List<string>> GetUserRoles(string email);
    }
}
