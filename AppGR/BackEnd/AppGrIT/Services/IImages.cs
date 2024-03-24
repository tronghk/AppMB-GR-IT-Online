using AppGrIT.Model;

namespace AppGrIT.Services
{
    public interface IImages
    {
        public Task<ResponseModel> AddImagesPostAsync(IFormFile fileImage, string nameStorage);
    }
}
