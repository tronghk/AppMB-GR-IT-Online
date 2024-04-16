using AppChat.Models;
using AppChat.Services;
using AppGrIT.Helper;
using AppGrIT.Model;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;

namespace AppChat.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MessageController : ControllerBase
    {
        private readonly IMessages _messageManager;

        public MessageController(IMessages messages)
        {

            _messageManager = messages;
        }

        [HttpPost("/create-chat-message")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> CreateChat([FromBody] ChatModel model)
        {
            if (model.UserId == model.UserOrtherId)
            {
                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Duplicate infor"
                });
            }
            var isUser = await _messageManager.CheckUserEx(model.UserId);
            var isUserM = await _messageManager.CheckUserEx(model.UserOrtherId);
            if (isUser && isUserM)
            {
                var result = await _messageManager.CreateChatModelAsync(model);
                if (result != null)
                {
                    return Ok(result);
                }
            }
            // kiem tra phai3 co tk va trung lap tk  và có đính vào unfollo


            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            });
        }
        [HttpPost("/create-message")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> CreateMessage([FromBody] DetailsChatModel model)
        {
            // check chat exist
            var isChat = await _messageManager.CheckChatEx(model.ChatId);
            if (!isChat)
            {
                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Chat unExist"
                });
            }
            // check user exist
            var isUser = await _messageManager.CheckUserEx(model.UserId);
            if (!isUser)
            {
                return NotFound(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = MessageResponse.MESSAGE_NOTFOUND
                });
            }
            if (model.Content == null && model.ImagePath == null)
            {
                return NotFound(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Content can not empty"
                });
            }
            var result = await _messageManager.CreateMessageModelAsync(model);
            if (result != null)
            {
                return Ok(result);
            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            });


        }
        [HttpPost("/create-group-chat-message")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> CreateGrChat([FromBody] GroupChatModel model)
        {
            var listGrMember = model.groupMembers;
            if (listGrMember != null)
            {
                if (listGrMember!.Count < 3)
                {
                    return BadRequest(new ResponseModel
                    {
                        Status = StatusResponse.STATUS_ERROR,
                        Message = MessageResponse.MESSAGE_CREATE_FAIL
                    });
                }



                var result = await _messageManager.CreateGroupModelAsync(model);

                if (result != null)
                {
                    var member = await _messageManager.CreateMemberGroupModelAsync(model.groupMembers!, result.GroupId);
                    if (member != null)
                    {
                        result.groupMembers = member;
                        return Ok(result);
                    }

                }

            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            });
        }
        [HttpDelete("/delete-chat-message")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> DeleteChat([FromBody] ChatModel model)
        {
            if (model.UserId == model.UserOrtherId)
            {
                return BadRequest(new ResponseModel
                {
                    Status = StatusResponse.STATUS_ERROR,
                    Message = "Duplicate infor"
                });
            }
            var isUser = await _messageManager.CheckUserEx(model.UserId);
            var isUserM = await _messageManager.CheckUserEx(model.UserOrtherId);
            if (isUser && isUserM)
            {
                var result = await _messageManager.DeleteChatModelAsync(model);


                if (result.Status == StatusResponse.STATUS_SUCCESS)
                {
                    return Ok(result);
                }

            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_DELETE_FAIL
            });
        }
        [HttpDelete("/delete-message")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> DeleteMessageChat([FromBody] DetailsChatModel model)
        {

            var isUser = await _messageManager.CheckUserEx(model.UserId);
            if (isUser)
            {
                var result = await _messageManager.DeleteMessageModelAsync(model);


                if (result.Status == StatusResponse.STATUS_SUCCESS)
                {
                    return Ok(result);
                }

            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_DELETE_FAIL
            });
        }
        [HttpDelete("/delete-group-chat-message")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> DeleteGroupChat([FromBody] GroupChatModel model)
        {
            if (model.GroupId == null || !await _messageManager.CheckChatEx(model.GroupId))
                return NotFound(new ResponseModel
                {
                    Status = StatusResponse.STATUS_NOTFOUND
                });

            var identity = HttpContext.User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var userId = identity.FindFirst("userId")!.Value;

                var member = model.groupMembers;
                var isValid = _messageManager.CheckRoleMember(userId, member, SynthesizeRoles.GR_MANAGER);
                if (isValid)
                {
                    // delete member
                    // delete message
                    // delete gr
                    var result = await _messageManager.DeleteGroupChatAsync(model);

                    if (result.Status == StatusResponse.STATUS_SUCCESS)
                    {
                        return Ok(result);
                    }
                }
                else
                {
                    return Unauthorized();
                }

            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_DELETE_FAIL
            });
        }
        [HttpPut("/update-group-chat-message")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> UpdateInforGroupChat([FromBody] GroupChatModel model)
        {
            // check gr co ton tai
            if (model.GroupId == null)
                return NotFound(new ResponseModel
                {
                    Status = StatusResponse.STATUS_NOTFOUND
                });

            var identity = HttpContext.User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var userId = identity.FindFirst("userId")!.Value;

                var member = model.groupMembers;
                var isValid = _messageManager.CheckRoleMember(userId, member, SynthesizeRoles.GR_MANAGER);
                if (isValid)
                {
                    
                    var result = await _messageManager.UpdateGroupModelAsync(model);

                    if (result!= null)
                    {
                        return Ok(result);
                    }
                }
                else
                {
                    return Unauthorized();
                }

            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_UPDATE_FAIL
            });
        }
        [HttpPost("/add-member-group-chat")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> AddMemberGroupChat([FromBody] GroupMemberModel model)
        {
            List<GroupMemberModel> members = new List<GroupMemberModel>();
            members.Add(model);
            // check gr co ton tai
            if (model.GroupId == null)
                return NotFound(new ResponseModel
                {
                    Status = StatusResponse.STATUS_NOTFOUND
                });

            var identity = HttpContext.User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var userId = identity.FindFirst("userId")!.Value;

                
                var isValid = _messageManager.CheckRoleMember(userId, members, SynthesizeRoles.GR_MANAGER);
                if (isValid)
                {

                    var result = await _messageManager.CreateMemberGroupModelAsync(members,model.GroupId);

                    if (result != null)
                    {
                        return Ok(result);
                    }
                }
                else
                {
                    return Unauthorized();
                }

            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_CREATE_FAIL
            });
        }
        [HttpDelete("/delete-member-group-chat")]
        [Authorize(Roles = SynthesizeRoles.CUSTOMER)]
        public async Task<IActionResult> DeleteMemberGroupChat([FromBody] GroupMemberModel model)
        {
            List<GroupMemberModel> members = new List<GroupMemberModel>();
            members.Add(model);
            // check gr co ton tai
            if (model.GroupId == null)
                return NotFound(new ResponseModel
                {
                    Status = StatusResponse.STATUS_NOTFOUND
                });

            var identity = HttpContext.User.Identity as ClaimsIdentity;
            if (identity != null)
            {
                var userId = identity.FindFirst("userId")!.Value;


                var isValid = _messageManager.CheckRoleMember(userId, members, SynthesizeRoles.GR_MANAGER);
                if (isValid)
                {

                    var result = await _messageManager.DeleteOneMemberGroupAsync(model);

                    if (result != null)
                    {
                        return Ok(result);
                    }
                }
                else
                {
                    return Unauthorized();
                }

            }
            return BadRequest(new ResponseModel
            {
                Status = StatusResponse.STATUS_ERROR,
                Message = MessageResponse.MESSAGE_DELETE_FAIL
            });
        }
    }

}
