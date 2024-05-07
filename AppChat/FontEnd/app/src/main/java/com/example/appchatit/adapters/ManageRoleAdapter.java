package com.example.appchatit.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.appchatit.R;
import com.example.appchatit.activities.ManageRoleActivity;
import com.example.appchatit.models.GroupMemberModel;
import com.example.appchatit.models.UserModel;
import com.example.appchatit.services.OnMemberListChangeListener;
import com.example.appchatit.services.OnRoleSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ManageRoleAdapter extends RecyclerView.Adapter<ManageRoleAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userList;
    private List<GroupMemberModel> memberList;
    public String currentGroupId;
    private OnRoleSelectedListener roleSelectedListener;
    private List<String> userIdList;

    public ManageRoleAdapter(Context context, List<UserModel> userList, List<GroupMemberModel> memberList, String chatId) {
        this.context = context;
        this.userList = userList;
        this.memberList = memberList;
        this.currentGroupId = chatId;

        userIdList = new ArrayList<>();
        for (GroupMemberModel member : memberList) {
            userIdList.add(member.getUserId());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.role_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comparator<GroupMemberModel> roleComparator = new Comparator<GroupMemberModel>() {
            @Override
            public int compare(GroupMemberModel member1, GroupMemberModel member2) {
                if ("GR_MANAGER".equals(member1.getRole())) {
                    return -1;
                } else if ("GR_MANAGER".equals(member2.getRole())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        Collections.sort(memberList, roleComparator);

        List<String> list = new ArrayList<String>();
        list.add("Manager");
        list.add("Sub-manager");
        list.add("Member");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(dataAdapter);

        GroupMemberModel memberModel = memberList.get(position);
        if (memberModel != null) {
            String role = memberModel.getRole();
            if ("GR_MANAGER".equals(role)) {
                holder.spinner.setSelection(0);
                holder.spinner.setEnabled(false);
            } else if ("GR_SUB_MANAGER".equals(role)) {
                holder.spinner.setSelection(1);
            } else {
                holder.spinner.setSelection(2);
            }
        }

        for (UserModel userModel : userList) {
            if (memberModel.getUserId().equals(userModel.getUserId())) {
                String otherUser = userModel.getUserName();
                if (otherUser != null && !otherUser.isEmpty()) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String myId = sharedPreferences.getString("userId", "");
                    if (userModel.getUserId().equals(myId)) {
                        holder.nameUser.setText(otherUser + " (You)");
                    } else {
                        holder.nameUser.setText(otherUser);
                    }
                } else {
                    holder.nameUser.setText("Unknown");
                }

                String imagePath = userModel.getImagePath();
                if (imagePath != null && !imagePath.isEmpty()) {
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transforms(new CircleCrop());
                    Glide.with(holder.imgUser.getContext())
                            .load(imagePath)
                            .apply(requestOptions)
                            .into(holder.imgUser);
                } else {
                    holder.imgUser.setImageResource(R.drawable.baseline_api_24);
                }
            }
        }

        String userId = userIdList.get(position);
        holder.spinner.setTag(userId);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public List<GroupMemberModel> getMemberList() {
        return memberList;
    }

    public void setOnRoleSelectedListener(OnRoleSelectedListener listener) {
        this.roleSelectedListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgUser;
        public TextView nameUser;
        public Spinner spinner;
        private Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.avt_role_item);
            nameUser = itemView.findViewById(R.id.name_role_item);
            spinner = itemView.findViewById(R.id.spinner_role);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedRole = (String) parentView.getItemAtPosition(position);

                    String userId = (String) parentView.getTag();

                    if (roleSelectedListener != null) {
                        roleSelectedListener.onRoleSelected(userId, position, selectedRole);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    //
                }
            });

        }

        @Override
        public void onClick(View v) {
            //
        }
    }
}