package com.example.appchatit.services;

import com.example.appchatit.models.ChatModel;
import com.example.appchatit.models.DetailsChatModel;
import com.example.appchatit.models.GroupChatModel;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.ResponseModel;
import com.example.appchatit.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ChatApiService {
    @GET("get-listMessOrtherUser")
    Call<List<UserModel>> getListMessOtherUser(
            @Header("Authorization") String token,
            @Query("userId") String userId
    );

    @GET("get-chat")
    Call<List<UserModel>> getChat(
            @Header("Authorization") String token,
            @Query("userId") String userId
    );

    @GET("get-listDetailsChat")
    Call<List<DetailsChatModel>> getListDetailsChat(
            @Header("Authorization") String token,
            @Query("chatId") String chatId
    );

    @POST("create-message")
    Call<DetailsChatModel> createMessage(
            @Header("Authorization") String token,
            @Body DetailsChatModel detailsChatModel
    );

    @POST("create-chat-message")
    Call<ChatModel> createChat(
            @Header("Authorization") String token,
            @Body ChatModel chatModel
    );

    @POST("create-group-chat-message")
    Call<GroupChatModel> createGroupChat(
            @Header("Authorization") String token,
            @Body GroupChatModel groupChatModel
    );

    @PUT("update-group-chat-message")
    Call<GroupChatModel> updateGroupChat(
            @Header("Authorization") String token,
            @Body GroupChatModel groupChatModel
    );

    @POST("add-member-group-chat")
    Call<GroupMemberModel> addMemberGroupChat(
            @Header("Authorization") String token,
            @Body GroupMemberModel groupMemberModel
    );

    @HTTP(method = "DELETE", path = "delete-member-group-chat", hasBody = true)
    Call<ResponseModel> deleteMemberGroupChat(
            @Header("Authorization") String token,
            @Body GroupMemberModel groupMemberModel
    );

    @GET("get-group-member")
    Call<List<GroupMemberModel>> getListMemberGroup(
            @Header("Authorization") String token,
            @Query("groupId") String groupId
    );

    @PUT("update-role-group-member")
    Call<GroupMemberModel> updateRoleGroupMember(
            @Header("Authorization") String token,
            @Query("groupId") String groupId,
            @Query("userMemberId") String userMemberId,
            @Query("roleName") String roleName
    );

    @PUT("update-role-admin-group-member")
    Call<ResponseModel> updateRoleAdminGroupMember(
            @Header("Authorization") String token,
            @Query("groupId") String groupId,
            @Query("userMemberId") String userMemberId
    );
}