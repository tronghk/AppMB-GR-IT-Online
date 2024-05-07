package com.example.appchatit.services;

import com.example.appchatit.models.GroupMemberModel;

import java.util.List;

public interface OnMemberListChangeListener {
    void onMemberListChange(List<GroupMemberModel> memberList);
}