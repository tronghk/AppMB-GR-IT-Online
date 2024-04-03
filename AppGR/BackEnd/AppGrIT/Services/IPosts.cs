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
        public Task<List<PostModel>> GetListPostSelfUser(string userId);

        public Task<PostModel> GetPostNewInfoUser(string userId);
        public Task<List<PostModel>> GetListAvatarPostUser(string userId);

        public Task<List<PostModel>> GetListCoverPostUser(string userId);

        public Task<PostModel> FindPostToIdAsync(string postId);
        public Task<PostSellProductModel> FindPostSellToIdAsync(string postSellId);
        public Task<PostModel> EditPostAsync(PostModel model);
        public Task<ResponseModel> DeletePostAsync(string postId);
        public Task<ResponseModel> DeletePostSellAsync(string postId);

        public Task<SharePostModel> SharePost(SharePostModel model);
        public Task<SharePostModel> GetPostShare(string postId, string userId);

        public Task<ResponseModel> DeleteSharePostAsync(string postId, string userId);
        public Task<List<SharePostModel>> GetListPostShared(string userId);



        public Task<ResponseModel> HiddenPost(string userId, string postId);

        public Task<PostSellProductModel> CreatePostSellAsync(PostSellProductModel model);
        public Task<PostSellProductModel> EditPostSellAsync(PostSellProductModel model);
    }
}