using AppGrIT.Models;

namespace AppGrIT.Services
{
    public interface IPostComments
    {
        public Task<List<string>> GetPostIdToUserFromPostComment(string userId);

        public Task<List<PostCommentModel>> GetPostComment(string postId);

        public Task<PostCommentModel> CreatePostCommentAsync(PostCommentModel model);
    }
}
