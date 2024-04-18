package com.example.appchatit.models;

import java.time.LocalDateTime;
import java.util.List;

public class GroupChatModel {
    private String groupId;
    private LocalDateTime timeCreate;
    private String groupName;
    private String imagePath;
    private List<GroupMemberModel> groupMembers;

    public GroupChatModel() {
    }

    public GroupChatModel(String groupId, LocalDateTime timeCreate, String groupName, String imagePath, List<GroupMemberModel> groupMembers) {
        this.groupId = groupId;
        this.timeCreate = timeCreate;
        this.groupName = groupName;
        this.imagePath = imagePath;
        this.groupMembers = groupMembers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public LocalDateTime getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(LocalDateTime timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<GroupMemberModel> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<GroupMemberModel> groupMembers) {
        this.groupMembers = groupMembers;
    }
}