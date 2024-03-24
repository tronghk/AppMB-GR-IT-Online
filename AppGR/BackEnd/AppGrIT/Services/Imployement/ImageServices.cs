using AppGrIT.Data;
using AppGrIT.Helper;
using AppGrIT.Model;
using Firebase.Auth;
using Firebase.Storage;
using FirebaseAdmin.Auth;
using FireSharp.Config;

namespace AppGrIT.Services.Imployement
{
    public class ImageServices : IImages
    {
        private readonly ImagesDAO _imageDAO;
        private readonly IConfiguration _configuration;
        private readonly FirebaseAuthProvider _firebaseAuth;
        private readonly string Bucket;

        public ImageServices(ImagesDAO image, IConfiguration configuration) {
        
            _imageDAO = image;
            _configuration = configuration;
            _firebaseAuth = new FirebaseAuthProvider(new Firebase.Auth.FirebaseConfig(_configuration["Firebase:API_Key"]));
            Bucket = _configuration["Firebase:Storage"]!;
        }

        public async Task<ResponseModel> AddImagesPostAsync(IFormFile fileImage, string nameStorage)    
        {

           
            var fileName = fileImage.FileName;
            nameStorage = nameStorage + fileName.Substring(fileName.Length - 4);
            FileStream stream;
            if(fileName.Length > 0)
            {
                string path = Path.Combine(Directory.GetCurrentDirectory(),"wwwroot","Images",fileName);
              

                using (var st = System.IO.File.Create(path))
                {
                    await fileImage.CopyToAsync(st);
                }
                  stream = new FileStream(Path.Combine(path), FileMode.Open);
                var link = await Upload(stream, nameStorage);
                if(link != null)
                {
                    if (File.Exists(path))
                    {
                        stream.Close();
                        File.Delete(path);
                    }
                    return new ResponseModel
                    {
                        Status = StatusResponse.STATUS_SUCCESS,
                        Message = link
                    };
                   
                }
                new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_CREATE_FAIL
                };

            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR
            };
        }
        public async Task<string> Upload(FileStream stream, string fileName)
        {
            string email = "user@example.com";
            string password = "string";
            var link = await _firebaseAuth.SignInWithEmailAndPasswordAsync(email, password);

           var cancellation = new CancellationTokenSource();
            var task = new FirebaseStorage(

                Bucket,
                new FirebaseStorageOptions
                {
                    AuthTokenAsyncFactory = () => Task.FromResult(link.FirebaseToken),
                    ThrowOnCancel = true
                }
                ).Child("images").Child(fileName).PutAsync(stream,cancellation.Token);
            try
            {
                string path = await task;
                return path;

            }catch(Exception ex)
            {
                return null!;
            }
        }
    }
}
