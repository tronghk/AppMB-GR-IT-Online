using AppGrIT.Model;
using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IPostComments
    {
        public Task<List<string>> GetPostIdToUserFromPostComment(string userId);

        public Task<List<PostCommentModel>> GetPostComment(string postId);

        public Task<PostCommentModel> CreatePostCommentAsync(PostCommentModel model);
        public Task<bool> CheckCommentDupUser(string postId, string commentId, string userId);
        public Task<ResponseModel> DeleteCommentAsync(string cmtId);
    }
}
