using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IPosts
    {
        public Task<PostModel> CreatePostAsync(PostModel model);
    }
}
