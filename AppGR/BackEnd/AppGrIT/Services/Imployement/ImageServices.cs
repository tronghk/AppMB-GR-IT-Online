using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using Firebase.Auth;
using Firebase.Storage;
using FirebaseAdmin.Auth;
using FireSharp.Config;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.DataProtection.KeyManagement;
using System;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace AppGrIT.Services.Imployement
{
    public class ImageServices : IImages
    {
        private readonly ImagesDAO _imageDAO;
        private readonly IConfiguration _configuration;
        private readonly FirebaseAuthProvider _firebaseAuth;
        private readonly FirebaseStorage _storage;
        private readonly string Bucket;

        public ImageServices(ImagesDAO image, IConfiguration configuration) {
        
            _imageDAO = image;
            _configuration = configuration;
            _firebaseAuth = new FirebaseAuthProvider(new Firebase.Auth.FirebaseConfig(_configuration["Firebase:API_Key"]));
            Bucket = _configuration["Firebase:Storage"]!;
            _storage= new FirebaseStorage(_configuration["Firebase:Storage"]);
        }

        public async Task<List<string>> AddImagesPostAsync(List<IFormFile> fileImage)    
        {

            var listLink = new List<string>();  
            foreach (IFormFile file in fileImage)
            {
                var fileName = file.FileName;
                DateTime dt = DateTime.Now; // Or whatever
                string s = dt.ToString("yyyyMMddHHmmss");
                string newName ="IMG-" + s + "-" + RandomChar() + fileName.Substring(fileName.Length-4);
                FileStream stream;
                if (fileName.Length > 0)
                {
                    string path = Path.Combine(Directory.GetCurrentDirectory(), "wwwroot", "Images", fileName);


                    using (var st = System.IO.File.Create(path))
                    {
                        await file.CopyToAsync(st);
                    }
                    stream = new FileStream(Path.Combine(path), FileMode.Open);
                    var link = await Upload(stream, newName);
                    if (link != null)
                    {
                        if (File.Exists(path))
                        {
                            stream.Close();
                            File.Delete(path);
                        }
                        listLink.Add(link);


                    }

                }
            }
            return listLink;
        }
        public static string RandomChar()
        {
            Random random = new Random();
            const string chars = "TeamITNeverDie";
            return new string(Enumerable.Repeat(chars, 6)
                .Select(s => s[random.Next(s.Length)]).ToArray());
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
        public async Task<PostImages> CreateImagePost(ImagePostModel model, string postId)
        {
            var postImage = new PostImages
            {
                ImageContent = model.ImageContent,
                ImagePath = model.ImagePath,
                PostId = postId
            };
           var result = await _imageDAO.CreateImageAsync(postImage);
            return result;
        }

        public async Task<List<ImagePostModel>> GetImagePostToId(string postId)
        {
           var list = await _imageDAO.GetImagePostToId(postId);
            var result = new List<ImagePostModel>();
            foreach (var imagePost in list)
            {
                var im = new ImagePostModel
                {
                    ImagePath = imagePost.ImagePath,
                    ImageContent = imagePost.ImageContent,
                    ImageId = imagePost.PostImageId,

                };
                result.Add(im);
            }
            return result;
        }
        public async Task<string> GetLinkAvatarDefault()
        {
            
            var starsRef = _storage.Child("IMG_Default").Child("sbcf-default-avatar.png");
            string link = await starsRef.GetDownloadUrlAsync();
            return link;
        }
        public async Task<string> GetLinkCoverDefault()
        {
           
            var starsRef = _storage.Child("IMG_Default").Child("cover_default.jpg");
            string link = await starsRef.GetDownloadUrlAsync();
            return link;
        }
        public async Task<string> DeleteFileStorageToPath(string url)
        {
            try
            {
                var filename = System.IO.Path.GetFileName(url).ToString();
            int start = filename.IndexOf("IMG-");
            int end = filename.IndexOf("?");
            filename = filename.Substring(start,end-start);
           

            var loginInfo = await _firebaseAuth.SignInWithEmailAndPasswordAsync("user@example.com", "string");
            var storage = new FirebaseStorage(Bucket, new FirebaseStorageOptions
            {
                AuthTokenAsyncFactory = () => Task.FromResult(loginInfo.FirebaseToken),
                ThrowOnCancel = false
            });

                
                
                await storage.Child("images").Child(filename).DeleteAsync();
                return MessageResponse.MESSAGE_DELETE_SUCCESS;
            }
            catch (Exception e)
            {
                var msg = e.Message;
                return MessageResponse.MESSAGE_DELETE_FAIL;
            }

        }
    }
}
