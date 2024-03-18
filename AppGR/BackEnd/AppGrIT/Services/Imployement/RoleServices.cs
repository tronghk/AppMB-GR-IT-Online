using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Model;
using AppGrIT.Models;
using Microsoft.AspNetCore.Identity;

namespace AppGrIT.Services.Imployement
{
    public class RoleServices : IRoles
    {
        private readonly RolesDAO _roleDao;
        private readonly UsersDAO _userDao;

        public RoleServices(RolesDAO role, UsersDAO user) {
            _roleDao = role;
            _userDao = user;
        }
        public async Task<ResponseModel> AddRoleAsync(RolesModel model)
        {
            Roles role = new Roles
            {
                RoleName = model.RoleName,
                RoleDescription = model.Despripsion
            };
            var result = await _roleDao.AddRoleAsync(role);
            return result;
        }

        public async Task<ResponseModel> AddUserRolesAsync(UserRoleModel model)
        {
           var user = await _userDao.GetUserAsync(model.Email);
            if(user == null)
            {
                return new ResponseModel
                {
                    Status = "Fail",
                    Message = ""
                };
            }
            Roles role = await _roleDao.GetRole(model.RoleName);
            if (role == null)
            {
                return new ResponseModel
                {
                    Status = "Fail",
                    Message = ""
                };
            }
            UserRoles us = new UserRoles
            {
                RoleId = role.IdRole,
                UserId = user.UserId
            };
            var result = await _roleDao.AddUserRoleAsync(us);
            return result;

        }

        public Task<ResponseModel> DeleteRoleAsync(RolesModel model)
        {
            throw new NotImplementedException();
        }

        public Task<ResponseModel> DeleteUserRolesAsync(UserRoleModel model)
        {
            throw new NotImplementedException();
        }

        public Task<Roles> GetRoleAsync(string roleName)
        {
           return _roleDao.GetRole(roleName);
        }
    }
}
