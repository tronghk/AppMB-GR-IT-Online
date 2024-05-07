using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using Microsoft.Extensions.Hosting;
using System.Collections.Generic;
using System.Reflection;
using static System.Net.Mime.MediaTypeNames;

namespace AppGrIT.Services.Imployement
{
    public class PostServices : IPosts
    {
        private readonly PostsDAO _postDAO;

        private readonly IImages _imageManager;
        private readonly IPostComments _postCommentManager;
        private readonly IPostExpressionss _postExpressionManager;
        private readonly ImagesDAO _imageDAO;

        public PostServices(PostsDAO postDAO, IImages imageManger, IPostExpressionss postpre, IPostComments postcmt, ImagesDAO image)
        {
            _postDAO = postDAO;
            _imageManager = imageManger;
            _postCommentManager = postcmt;
            _postExpressionManager = postpre;
            _imageDAO = image;
        }

        public async Task<PostModel> EditImagePostInstead(PostModel model)
        {
            var post = new Posts
            {
                PostId = model.PostId!,
                UserId = model.UserId!,
                Content = model.Content!,
                PostTime = model.PostTime,
                PostType = model.PostType!,
            };
            var p = await _postDAO.EditPostAsync(post);
            var postModel = new PostModel
            {
                PostId = p.PostId,
                UserId = p.UserId!,
                Content = p.Content!,
                PostTime = p.PostTime,
                PostType = p.PostType!,
                imagePost = model.imagePost
            };

            return postModel;
        }

        public async Task<PostModel> CreatePostAsync(PostModel model)
        {
            var post = new Posts
            {
                UserId = model.UserId!,
                Content = model.Content!,
                PostTime = model.PostTime,
                PostType = model.PostType!,
            };
            var p = await _postDAO.CreatePostAsync(post);

            var list = model.imagePost;
            List<ImagePostModel> images = new List<ImagePostModel>();
            foreach (var image in list)
            {
                var imageP = await _imageManager.CreateImagePost(image, p.PostId);
                var im = new ImagePostModel
                {
                    ImagePath = imageP.ImagePath,
                    ImageContent = imageP.ImageContent,
                    ImageId = imageP.PostImageId,

                };
                images.Add(im);
            }

            var postModel = new PostModel
            {
                PostId = p.PostId,
                UserId = p.UserId!,
                Content = p.Content!,
                PostTime = p.PostTime,
                PostType = p.PostType!,
                imagePost = images
            };

            return postModel;
        }
        public async Task<PostModel> EditPostAsync(PostModel model)
        {
            var post = new Posts
            {
                PostId = model.PostId!,
                UserId = model.UserId!,
                Content = model.Content!,
                PostTime = model.PostTime,
                PostType = model.PostType!,
            };
            if (!await CheckContent(model.Content!, model.PostId!))
            {
                var p = await _postDAO.EditPostAsync(post);
                post = p;
            }

            

            var list = model.imagePost;
            var listPost = await _imageManager.GetImagePostToId(model.PostId!);
            foreach ( var imagePost in list )
            {
                var result = CheckImageExsist(imagePost, listPost);
                if (result != null)
                {
                    if (!CheckImageContentDup(result, imagePost))
                    {
                        // khác content
                        var postImage = new PostImages
                        {
                            PostImageId = result.ImageId!,
                            ImageContent = imagePost.ImageContent!,
                            ImagePath = result.ImagePath!,
                            PostId = model.PostId!
                        };
                        await _imageDAO.UpdateImageAsync(postImage);
                    }
                    if(!CheckImagePathDup(result, imagePost))
                    {
                        // xóa hình trong storage
                        await _imageManager.DeleteFileStorageToPath(result.ImagePath!);

                        // cập nhật path mới
                        var postImage = new PostImages
                        {
                            PostImageId = result.ImageId!,
                            ImageContent = result.ImageContent!,
                            ImagePath = imagePost.ImagePath!,
                            PostId = model.PostId!
                        };
                        await _imageDAO.UpdateImageAsync(postImage);
                    }
                    // xóa khỏi danh sách chờ
                    int index = FindIndexImageInList(result.ImageId!, listPost);
                    listPost.RemoveAt(index);
                }
                else
                {
                    // tạo mới hình ảnh
                    var postImage = new PostImages
                    {
                        ImageContent = imagePost.ImageContent!,
                        ImagePath = imagePost.ImagePath!,
                        PostId = model.PostId!
                    };
                    await _imageDAO.CreateImageAsync(postImage);
                }
            }
            // xóa những image cũ còn tồn tại trong db
            foreach(var value in listPost)
            {
                // xóa hình trong storage
                await _imageManager.DeleteFileStorageToPath(value.ImagePath!);
                // xóa db
                await _imageDAO.DeleteImagePost(value.ImageId!);
            }
            var listImage = await _imageManager.GetImagePostToId(model.PostId!);
            var postModel = new PostModel
            {
                PostId = post.PostId,
                UserId = post.UserId!,
                Content = post.Content!,
                PostTime = post.PostTime,
                PostType = post.PostType!,
                imagePost = listImage
            };
            return postModel;
        }
        public async Task<ResponseModel> DeletePostAsync(string postId)
        {
           
           
            var listPost = await _imageManager.GetImagePostToId(postId);
          
            
            // xóa những image cũ còn tồn tại trong db
            foreach (var value in listPost)
            {
                // xóa hình trong storage
                await _imageManager.DeleteFileStorageToPath(value.ImagePath!);
                // xóa db
                await _imageDAO.DeleteImagePost(value.ImageId!);
            }
            var result = await _postDAO.DeletePost(postId);
            if (result.Equals(MessageResponse.MESSAGE_DELETE_SUCCESS))
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = result
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = result
            };
        }
        public int FindIndexImageInList(string imageId, List<ImagePostModel> list)
        {
            int index = 0;
            foreach (var value in list)
            {
                if (value.ImageId!.Equals(imageId))
                    return index;
                index++;
            }
            return -1;
        }
        public ImagePostModel CheckImageExsist(ImagePostModel imagePost, List<ImagePostModel> list)
        {
           
            foreach(var value in list)
            {
                if (value.ImageId!.Equals(imagePost.ImageId))
                        return value;
            }
            return null!;
        }
        public bool CheckImageContentDup(ImagePostModel imagePostNew, ImagePostModel imagePostOld)
        {
            if(imagePostNew.ImageContent!.Equals(imagePostOld.ImageContent))
                return true;
            return false;
        }
        public bool CheckImagePathDup(ImagePostModel imagePostNew, ImagePostModel imagePostOld)
        {
            if (imagePostNew.ImagePath!.Equals(imagePostOld.ImagePath))
                return true;
            return false;
        }
        public async Task<bool> CheckContent(string content, string postId)
        {
            var post = await _postDAO.GetPosts(postId);
            if (post.Content.Equals(content))
                return true;
            return false;
        }

        public async Task<List<PostModel>> GetListPostUser(string userId)
        {
            List<string> listComt = await _postCommentManager.GetPostIdToUserFromPostComment(userId);
            List<string> listExp = await _postExpressionManager.GetPostIdToUserFromPostExpression(userId);
            var result = await _postDAO.GetPostsAsync(listComt, listExp);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
            {
                if(!await _postDAO.CheckPostHidden(post.PostId,userId))

                {
                    var p = new PostModel
                    {
                        PostId = post.PostId,
                        Content = post.Content,
                        PostTime = post.PostTime,
                        PostType = post.PostType,
                        UserId = post.UserId!,
                    };

                    var listImage = await _imageManager.GetImagePostToId(p.PostId);
                    p.imagePost = listImage;
                    list.Add(p);
                }
            }
            return list;
        }

        public async Task<List<PostModel>> GetListPostSelfUser(string userId)
        {
          
            var result = await _postDAO.GetPostsSelfAsync(userId);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
            {

                var p = new PostModel
                {
                    PostId = post.PostId,
                    Content = post.Content,
                    PostTime = post.PostTime,
                    PostType = post.PostType,
                    UserId = post.UserId!,
                };

                var listImage = await _imageManager.GetImagePostToId(p.PostId);
                p.imagePost = listImage;
                list.Add(p);
            }
            return list;
        }
        public async Task<List<PostModel>> GetListAvatarPostUser(string userId)
        {
            var result = await _postDAO.GetPostsAvatarAsync(userId);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
            {
               
                    var p = new PostModel
                    {
                        PostId = post.PostId,
                        Content = post.Content,
                        PostTime = post.PostTime,
                        PostType = post.PostType,
                        UserId = post.UserId!,
                    };

                    var listImage = await _imageManager.GetImagePostToId(p.PostId);
                    p.imagePost = listImage;
                    list.Add(p);
                

            }
            return list;
        }
        public async Task<List<PostModel>> GetListCoverPostUser(string userId)
        {
            var result = await _postDAO.GetPostsCoverAsync(userId);
            List<PostModel> list = new List<PostModel>();
            foreach (var post in result)
            {

                var p = new PostModel
                {
                    PostId = post.PostId,
                    Content = post.Content,
                    PostTime = post.PostTime,
                    PostType = post.PostType,
                    UserId = post.UserId!,
                };

                var listImage = await _imageManager.GetImagePostToId(p.PostId);
                p.imagePost = listImage;
                list.Add(p);


            }
            return list;
        }
        public async Task<PostModel> GetPostNewInfoUser(string userId)
        {
            var list = await _postDAO.GetUserInstead(userId);

            var idNew = list[0];
            foreach (var post in list)
            {
                if (post.PostTime > idNew.PostTime)
                {
                    idNew = post; break;
                }
            }
            var listImage = await _imageManager.GetImagePostToId(idNew.PostId);

            return new PostModel
            {
                PostId = idNew.PostId,
                Content = idNew.Content,
                PostTime = idNew.PostTime,
                PostType = idNew.PostType,
                UserId = idNew.UserId!,
                imagePost = listImage
            };

        }

        public async Task<PostModel> FindPostToIdAsync(string postId)
        {
            var idNew = await _postDAO.GetPosts(postId);
            if (idNew == null)
                return null;
            var listImage = await _imageManager.GetImagePostToId(idNew.PostId);
            var postModel = new PostModel
            {
                PostId = idNew.PostId,
                Content = idNew.Content,
                PostTime = idNew.PostTime,
                PostType = idNew.PostType,
                UserId = idNew.UserId!,
                imagePost = listImage
            };
            return postModel;
        }

        public async Task<SharePostModel> SharePost(SharePostModel model)
        {
            PostShares post = new PostShares
            {
                PostId = model.PostId,
                UserId = model.UserId,
                ContentShare = model.Content,
                TimeShare = model.TimeShare
            };
            var result = await _postDAO.CreatePostShareAsync(post);
            if (result != null)
                return model;
            return null;
        }

        public async Task<SharePostModel> GetPostShare(string postId, string userId)
        {
            PostShares post = await _postDAO.GetPostShare(postId, userId);
            if(post!= null)
            {
                var postModel = new SharePostModel
                {
                    PostId = post.PostId,
                    UserId = post.UserId,
                    Content = post.ContentShare,
                    TimeShare = post.TimeShare
                };
                return postModel;
            }
            return null!;
        }

        public async Task<ResponseModel> DeleteSharePostAsync(string postId, string userId)
        {
            var posts = new PostShares
            {
                PostId = postId,
                UserId = userId,
            };
           var result = await _postDAO.DeleteSharePostAsync(posts);
            if (result.Equals(StatusResponse.STATUS_SUCCESS))
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = MessageResponse.MESSAGE_DELETE_SUCCESS
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_DELETE_FAIL
            };
        }

        public async Task<List<SharePostModel>> GetListPostShared(string userId)
        {
            var list = await _postDAO.GetListPostShare(userId);
            var listModel = new List<SharePostModel>();
            foreach(var post in list)
            {
                var postModel = new SharePostModel
                {
                    PostId = post.PostId,
                    UserId = post.UserId,
                    Content = post.ContentShare,
                    TimeShare = post.TimeShare
                };
                listModel.Add(postModel);
            }
            return listModel;
        }

        public async Task<ResponseModel> HiddenPost(string userId, string postId)
        {
            var post = new PostHidden
            {
                UserId = userId,
                PostId = postId
            };
            var reult = await _postDAO.HiddenPost(post);
            if (reult.Equals(StatusResponse.STATUS_SUCCESS))
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = MessageResponse.MESSAGE_CREATE_SUCCESS
                };
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            };
        }

        public async Task<PostSellProductModel> CreatePostSellAsync(PostSellProductModel model)
        {
            var post = new PostSellProduct
            {
                UserId = model.UserId!,
                Content = model.Content!,
                PostTime = model.PostTime,
                Price = model.Price,
                ProductName = model.ProductName,
               
            };
            var p = await _postDAO.CreatePostSellAsync(post);
            if (p == null)
                return null!;
            var list = model.imagePosts;
            List<ImagePostModel> images = new List<ImagePostModel>();
            foreach (var image in list)
            {
                var imageP = await _imageManager.CreateImagePost(image, p.PostSellProductId);
                var im = new ImagePostModel
                {
                    ImagePath = imageP.ImagePath,
                    ImageContent = imageP.ImageContent,
                    ImageId = imageP.PostImageId,

                };
                images.Add(im);
            }

            var postModel = new PostSellProductModel
            {
                PostSellProductId = p.PostSellProductId,
                UserId = p.UserId!,
                Content = p.Content!,
                PostTime = p.PostTime,
                imagePosts = images,
                ProductName = p.ProductName,
            };

            return postModel;
        }

        public async Task<PostSellProductModel> EditPostSellAsync(PostSellProductModel model)
        {
            var post = new PostSellProduct
            {
                PostSellProductId = model.PostSellProductId!,
                UserId = model.UserId!,
                Content = model.Content!,
                PostTime = model.PostTime,
                ProductName = model.ProductName
                ,
                Price = model.Price
            };
            var p = await _postDAO.EditPostSellAsync(post);
            var postModel = new PostSellProductModel
            {
                PostSellProductId = p.PostSellProductId,
                UserId = p.UserId!,
                Content = p.Content!,
                PostTime = p.PostTime,
                Price = p.Price,
                ProductName = p.ProductName

            };
            var image = model.imagePosts;
            var listPost = await _imageManager.GetImagePostToId(model.PostSellProductId);
            foreach (var imagePost in image)
            {
                var result = CheckImageExsist(imagePost, listPost);
                if (result != null)
                {
                    if (!CheckImageContentDup(result, imagePost))
                    {
                        // khác content
                        var postImage = new PostImages
                        {
                            PostImageId = result.ImageId!,
                            ImageContent = imagePost.ImageContent!,
                            ImagePath = result.ImagePath!,
                            PostId = model.PostSellProductId!
                        };
                        await _imageDAO.UpdateImageAsync(postImage);
                    }
                    if (!CheckImagePathDup(result, imagePost))
                    {
                        // xóa hình trong storage
                        await _imageManager.DeleteFileStorageToPath(result.ImagePath!);

                        // cập nhật path mới
                        var postImage = new PostImages
                        {
                            PostImageId = result.ImageId!,
                            ImageContent = result.ImageContent!,
                            ImagePath = imagePost.ImagePath!,
                            PostId = model.PostSellProductId!
                        };
                        await _imageDAO.UpdateImageAsync(postImage);
                    }
                    // xóa khỏi danh sách chờ
                    int index = FindIndexImageInList(result.ImageId!, listPost);
                    listPost.RemoveAt(index);
                }
                else
                {
                    // tạo mới hình ảnh
                    var postImage = new PostImages
                    {
                        ImageContent = imagePost.ImageContent!,
                        ImagePath = imagePost.ImagePath!,
                        PostId = model.PostSellProductId!
                    };
                    await _imageDAO.CreateImageAsync(postImage);
                }
            }
            foreach (var value in listPost)
            {
                // xóa hình trong storage
                await _imageManager.DeleteFileStorageToPath(value.ImagePath!);
                // xóa db
                await _imageDAO.DeleteImagePost(value.ImageId!);
            }
            var images = await _imageManager.GetImagePostToId(model.PostSellProductId);
            postModel.imagePosts = images;
            return postModel;
        }

        public async Task<PostSellProductModel> FindPostSellToIdAsync(string postSellId)
        {
            var idNew = await _postDAO.GetPostsSell(postSellId);
            if (idNew == null)
                return null;
            var listImage = await _imageManager.GetImagePostToId(idNew.PostSellProductId);
            var postModel = new PostSellProductModel
            {
                PostSellProductId = idNew.PostSellProductId,
                Content = idNew.Content,
                PostTime = idNew.PostTime,
                ProductName = idNew.ProductName,
                UserId = idNew.UserId!,
                imagePosts = listImage
            };
            return postModel;
        }

        public async Task<ResponseModel> DeletePostSellAsync(string postId)
        {
            var listPost = await _imageManager.GetImagePostToId(postId);


            // xóa những image cũ còn tồn tại trong db
            foreach (var value in listPost)
            {
                // xóa hình trong storage
                await _imageManager.DeleteFileStorageToPath(value.ImagePath!);
                // xóa db
                await _imageDAO.DeleteImagePost(value.ImageId!);
            }
            var result = await _postDAO.DeletePostSell(postId);
            if (result.Equals(MessageResponse.MESSAGE_DELETE_SUCCESS))
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_SUCCESS,
                    Message = result
                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = result
            };
        }
        public async Task<List<PostModel>> FindPostByContent(string Content)
        {
            List<PostModel> result = new List<PostModel>();
            var list = await _postDAO.FindPostBySubstringContentAsync(Content);
            foreach (var post in list)
            {

                var listImage = await _imageManager.GetImagePostToId(post.PostId);                           
                var us = new PostModel
                {
                    PostId = post.PostId,
                    Content = post.Content,
                    PostTime = post.PostTime,
                    PostType = post.PostType,
                    UserId = post.UserId!,
                    imagePost =listImage,                 
                };
                result.Add(us);
            }
            return result;
        }

        public async Task<int> GetSumPostDay()
        {
           DateTime date = DateTime.Now;
            var count = await _postDAO.GetSumPostOfDay(date);
            return count;
           
        }

        public async Task<int> GetSumPostWeek()
        {
            DateTime date = DateTime.Now;
            var count = await _postDAO.GetSumPostOWeek(date);
            return count;

        }

        public async Task<int> CompareGainPostLastWeek()
        {
            DateTime date = DateTime.Now;
            var count = await _postDAO.GetSumPostOWeek(date);
          
            var day = date.Day;
            var month = date.Month;
            var sttDay = GetSttDayOfMonth(date);
            while(sttDay != 1)
            {
                day = day - 1;
                sttDay = sttDay - 1; 
            }
            if(day <= 0)
            {
                month = month - 1;
                var d = 30;
                if(month%2 != 0)
                {
                    d = 31;
                }
                while(day < 0)
                {
                    day = day + 1;
                    d = d - 1;
                }
                day = d;
            }
            string dateTime = month+"/"+day+"/"+date.Year;
            DateTime dt = Convert.ToDateTime(dateTime);
            var re = await _postDAO.GetSumPostOWeek(dt);
            return re-count;

        }
        public int GetSttDayOfMonth(DateTime date)
        {
            var day = date.Day;
            var year = date.Year;
            var month = date.Month;
            var differenceYear = year - 2020;
            var leapYear = differenceYear / 4 + 1;
            if(month < 2  && year / 100 != 0 && year / 4 == 0)
            {
                leapYear = leapYear -1;
            }
            if (month == 2 && year / 100 != 0 && year / 4 == 0 && day < 29)
            {
                leapYear = leapYear - 1;
            }

            var sumDay = differenceYear * 365 + leapYear;
            sumDay = sumDay + sumDayOfMonth(month) + day;

            var dayOfToday = (sumDay-1) % 7;
            var sttDayToDay = -1;
            if(dayOfToday > DateOfMonth.FirstDayOfWeek1Year2020)
            {
                sttDayToDay = dayOfToday - DateOfMonth.FirstDayOfWeek1Year2020 + 1;
            }
            else
            {
                sttDayToDay = DateOfMonth.FirstDayOfWeek1Year2020 + dayOfToday;
            }
            return sttDayToDay;
        }
        public int sumDayOfMonth(int month)
        {
            var day = 0;
            for(int i = 1;i< month; i++)
            {
                if(i%2 != 0)
                {
                    day = day + 31;
                }
                else if(i == 2)
                {
                    day = day + 28;
                }
                else
                {
                    day = day + 30;
                }

            }
            return day;
        }
    }
}
