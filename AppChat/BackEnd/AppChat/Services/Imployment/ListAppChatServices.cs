using AppChat.Data;
using AppChat.Entity;
using AppChat.Models;
using AppGrIT.Models;

namespace AppChat.Services.Imployment
{
    public class ListAppChatServices : IListAppChat
    {
        private readonly ListAppChatDAO _ListAppChatDao;

        public ListAppChatServices(ListAppChatDAO listchat)
        {
            _ListAppChatDao = listchat;
        }

        public async Task<List<UserModel>> GetListMess(string userId)
        {
            List<UserModel> result = new List<UserModel>();
            var list = await _ListAppChatDao.GetListMess(userId);

            foreach (var userFriends in list)
            {
                var userinfo = await _ListAppChatDao.GetUserInforAsync(userFriends.UserOrtherId);
                var post = await GetPostNewInfoUser(userFriends.UserOrtherId);
                var image = post.imagePost;
                var pathImage = image[0].ImagePath;
                var userModel = new UserModel
                {
                    UserId = userFriends.UserOrtherId,
                    UserName = userinfo.LastName + " " + userinfo.Firstname,
                    ImagePath = pathImage,
                };
                result.Add(userModel);
            }
            return result;
        }
        private async Task<PostModel> GetPostNewInfoUser(string userId)
        {
            var list = await _ListAppChatDao.GetUserInstead(userId);

            var idNew = list[0];
            foreach (var post in list)
            {
                if (post.PostTime > idNew.PostTime)
                {
                    idNew = post;
                }
            }
            var listImage = await GetImagePostToId(idNew.PostId);

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
        public async Task<List<ImagePostModel>> GetImagePostToId(string postId)
        {
            var list = await _ListAppChatDao.GetImagePostToId(postId);
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

        public async Task<List<DetailsChatModel>> GetListDetailsChat(string chatId)
        {
           
                List<DetailsChatModel> result = new List<DetailsChatModel>();
                var list = await _ListAppChatDao.GetListDetailschat(chatId);
                foreach (var userFriends in list)
                {
                var us = new DetailsChatModel
                {
                    ChatId = userFriends.ChatId,
                    Content = userFriends.Content,
                    DetailId = userFriends.DetailId,
                    ImagePath = userFriends.ImagePath,
                    Status = userFriends.Status,    
                    Time = userFriends.Time,
                    UserId = userFriends.UserId,
                
                    };
                    result.Add(us);
                }
                return result;
            }
        
    }
}
