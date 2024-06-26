﻿using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
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
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_CREATE_FAIL
                };
            }
            Roles role = await _roleDao.GetRole(model.RoleName);
            if (role == null)
            {
                await AddRoleAsync(new RolesModel
                {
                    RoleName = model.RoleName,
                    Despripsion = ""
                });
                role = await _roleDao.GetRole(model.RoleName);
            }
            UserRoles us = new UserRoles
            {
                RoleId = role.RoleId,
                UserId = user.UserId
            };
            var listRole = await GetUserRoles(user.UserId);
            if (!CheckDupRole(listRole,model.RoleName))
            {
                var result = await _roleDao.AddUserRoleAsync(us);
                return result;
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_SUCCESS,
                Message = "Duplicate"
            };
           

        }
        public bool CheckDupRole(List<string> role, string roleName)
        {
            foreach (var value  in role)
            {
                if (value == roleName)
                    return true;

            }
            return false;
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

        public async Task<List<string>> GetUserRoles(string userId)
        {
            List<UserRoles> listUserRoles = await _roleDao.GetUserRolesAsync(userId);
            List<string> listRoles = await _roleDao.GetRoleNameAsync(listUserRoles);
            return listRoles;
        }
    }
}
