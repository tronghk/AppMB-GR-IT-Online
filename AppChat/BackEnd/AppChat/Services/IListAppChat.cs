using AppChat.Models;
using AppGrIT.Models;

namespace AppChat.Services
{
    public interface IListAppChat
    {
        public Task<List<UserModel>> GetListMess(string userId);
      
   }
}
