using AppGrIT.Entity;
using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IImages
    {
        public Task<List<string>> AddImagesPostAsync(List<IFormFile> fileImage);
        public Task<PostImages> CreateImagePost(ImagePostModel model, string postId);
        public Task<List<ImagePostModel>> GetImagePostToId(string postId);
    }
}
