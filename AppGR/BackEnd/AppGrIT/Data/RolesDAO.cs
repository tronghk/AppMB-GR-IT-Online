using AppGrIT.Entity;
using AppGrIT.Model;
using FireSharp.Response;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System.Security.Principal;
using FireSharp;
using Firebase.Auth;
using AppGrIT.Helper;

namespace AppGrIT.Data
{
    public class RolesDAO
    {
        private readonly ConnectFirebase _firebase;
        public RolesDAO(ConnectFirebase connectFirebase)
        {
            _firebase = connectFirebase;
        }
        public async Task<ResponseModel> AddUserRoleAsync(UserRoles userRoles)
        {
            try
            {
                // path UserRoles/data
                PushResponse response = await _firebase._client.PushAsync("UserRoles/",userRoles);
                
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_OK,
                    Message = MessageResponse.MESSAGE_CREATE_SUCCESS

                };
            }
            catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = ex.Message
                };
            }
        }
        public async Task<ResponseModel> AddRoleAsync(Roles role)
        {
            try
            {
                // path Role/data
                PushResponse response = await _firebase._client.PushAsync("Roles/", role);
                role.RoleId = response.Result.name;
                await _firebase._client.SetAsync("Roles/" + role.RoleId, role);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_OK,
                    Message = "Success"

                };
            }
            catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = ex.Message
                };
            }
        }
        public async Task<List<Roles>> GetAllRoles()
        {
            FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Roles");
            dynamic data = JsonConvert.DeserializeObject<dynamic>(firebaseResponse.Body);
            var list = new List<Roles>();
            if (data != null)
            {
                foreach (var item in data)
                {
                    Roles us = JsonConvert.DeserializeObject<Roles>(((JProperty)item).Value.ToString());
                    list.Add(us);
                }
            }
            return list;
        }
        public async Task<List<UserRoles>> GetUserRolesAsync(string UserId)
        {
            try
            {
                //path array UserRoles/all
                FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("UserRoles");
                dynamic data = JsonConvert.DeserializeObject<dynamic>(firebaseResponse.Body);
                var list = new List<UserRoles>();
                if(data != null)
                {
                    foreach (var item in data)
                    {
                        UserRoles us = JsonConvert.DeserializeObject<UserRoles>(((JProperty)item).Value.ToString());
                        if (us.UserId == UserId)
                            list.Add(us);
                    }
                }
                return list;
               
            }
            catch (Exception ex)
            {
                return new List<UserRoles>();
            }
        }
        public async Task<List<string>> GetRoleNameAsync(List<UserRoles> list)
        {
            List<string> roles = new List<string>();
            List<Roles> rolesList = await GetAllRoles();
            foreach (var value in list)
            {
                foreach (var role in rolesList)
                {
                    if (role.RoleId.Equals(value.RoleId))
                    {
                        roles.Add(role.RoleName);
                        break;
                    }
                }
            }
            return roles;
          
        }
        public async Task<Roles> GetRole(string roleName)
        {
            Roles us = null!;
            try
            {
                //path array UserRoles/all
                FirebaseResponse firebaseResponse = await _firebase._client.GetAsync("Roles");
                dynamic data = JsonConvert.DeserializeObject<dynamic>(firebaseResponse.Body);
                bool result = false;
                if (data != null)
                {
                    foreach (var item in data)
                    {
                        us = JsonConvert.DeserializeObject<Roles>(((JProperty)item).Value.ToString());
                        if (us.RoleName == roleName)
                        {
                            result = true;
                            break;
                        }
                    }
                }
               
               
               if(result)
                    return us;
                return null;
            }
            catch (Exception ex)
            {
                return null!;
            }
           
        }
        public async Task<ResponseModel> DeleteUserRolesAsync(string UserId, string RoleId)
        {
            try
            {
                //path UserRoles/all
                FirebaseResponse response = await _firebase._client.GetAsync("UserRoles");
                //path All Id
                JObject jsonResponse = response.ResultAs<JObject>();
                var value = "";
                foreach (var item in jsonResponse)
                {
                    value = item.Value!.ToString();
                    //path Object
                    var us = JsonConvert.DeserializeObject<UserRoles>(value);
                    if (us.UserId == UserId && us.RoleId == RoleId)
                        break;
                }
                if (value.Equals(""))
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_ERROR,
                        Message = "Can not find"
                    };

                 await _firebase._client.DeleteAsync("UserRoles/" +value);
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_OK,
                    Message = "Success"
                };


            }
            catch (Exception ex)
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = ex.Message
                };
            }
        }

    }
}
