using AppGrIT.Entity;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IPosts
    {
        public Task<PostModel> CreatePostAsync(PostModel model);
        public Task<PostModel> EditImagePostInstead(PostModel model);
        public Task<List<PostModel>> GetListPostUser(string userId);

        public Task<PostModel> GetPostNewInfoUser(string userId);
        public Task<List<PostModel>> GetListAvatarPostUser(string userId);

        public Task<List<PostModel>> GetListCoverPostUser(string userId);



    }
}
