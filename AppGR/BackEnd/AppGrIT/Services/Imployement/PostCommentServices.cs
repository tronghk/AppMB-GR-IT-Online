﻿
using AppGrIT.Data;
using AppGrIT.Entity;
using AppGrIT.Helper;
using AppGrIT.Model;
using AppGrIT.Models;
using Microsoft.AspNetCore.Identity;

namespace AppGrIT.Services.Imployement
{
    public class PostCommentServices : IPostComments
    {
        private readonly PostCommentsDAO _postcmtDAO;
        private readonly IUsers _userManager;

        public PostCommentServices(PostCommentsDAO post, IUsers user)
        {
            _postcmtDAO = post;
            _userManager = user;
        }

        public async Task<PostCommentModel> CreatePostCommentAsync(PostCommentModel model)
        {
            var postCmt = new PostComments
            {
                PostId = model.PostId,
                UserId = model.UserId,
                Content = model.Content,
                CommentTime = model.CommentTime,

            };
            var result = await _postcmtDAO.CreatePostCommentAsync(postCmt);
            var icon = new PostCommentModel
            {
                CommentId = result.CommentId,
                UserId = result.UserId,
                PostId = result.PostId,
                CommentTime = result.CommentTime,
                Content = result.Content
            };
            return icon;
        }

        public async Task<List<PostCommentModel>> GetPostComment(string postId)
        {
            var result = await _postcmtDAO.GetPostComment(postId);
            var list = new List<PostCommentModel>();
            foreach (var item in result)
            {
                var icon = new PostCommentModel
                {
                    UserId = item.UserId,
                    PostId = postId,
                    CommentTime = item.CommentTime,
                    Content = item.Content
                };
               
                list.Add(icon);
            }
            return list;
        }

        public async Task<List<string>> GetPostIdToUserFromPostComment(string userId)
        {
            return await _postcmtDAO.GetPostIdToUserFromPostComment(userId);
        }
        public async Task<bool> CheckCommentDupUser(string postId, string commentId, string userId)
        {
            var list = await _postcmtDAO.GetPostComment(postId);
            foreach (var item in list)
            {
                if(item.UserId == userId &&  item.CommentId.Equals(commentId))
                    return true;
            }
            return false;
        }

        public async Task<ResponseModel> DeleteCommentAsync(string cmtId)
        {
           var result = await _postcmtDAO.DeletePostCommentAsync(cmtId);
            if (result.Equals(MessageResponse.MESSAGE_DELETE_FAIL))
            {
                return new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_DELETE_FAIL,

                };
            }
            return new ResponseModel
            {
                Status = StatusResponse.STATUS_SUCCESS,
                Message = MessageResponse.MESSAGE_DELETE_SUCCESS,

            };
        }

        public async Task<PostCommentModel> GetCommentAsync(string postId, string cmtId)
        {
            var result = await _postcmtDAO.GetPostComment(postId,cmtId);
            if(result!= null)
            {
                var icon = new PostCommentModel
                {
                    UserId = result.UserId,
                    PostId = postId,
                    CommentTime = result.CommentTime,
                    Content = result.Content
                };

                return icon;

            }
            return null!;
               
        }
    }
}
