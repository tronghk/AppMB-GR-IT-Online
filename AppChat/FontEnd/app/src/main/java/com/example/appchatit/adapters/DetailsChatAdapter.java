package com.example.appchatit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appchatit.R;
import com.example.appchatit.models.DetailsChatModel;

import java.util.List;

public class DetailsChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String userId;
    private Context context;
    public static List<DetailsChatModel> detailsChatModelList;

    public DetailsChatAdapter(Context context, List<DetailsChatModel> list, String userId) {
        this.userId = userId;
        this.context = context;
        this.detailsChatModelList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.message_left_boxchat, parent, false);
            return new ViewHolder1(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_right_boxchat, parent, false);
            return new ViewHolder2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DetailsChatModel list = detailsChatModelList.get(position);
        String message = list.getContent();

        if (holder instanceof ViewHolder1) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            if (message != null && !message.isEmpty()) {
                viewHolder1.messageLeft.setText(message);
            } else {
                viewHolder1.messageLeft.setText("Error");
            }
        } else if (holder instanceof ViewHolder2) {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            if (message != null && !message.isEmpty()) {
                viewHolder2.messageRight.setText(message);
            } else {
                viewHolder2.messageRight.setText("Error");
            }
        }
    }

    @Override
    public int getItemCount() {
        return detailsChatModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (detailsChatModelList.get(position).getUserId().equals(userId)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView messageLeft;
        private Context mContext;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            messageLeft = itemView.findViewById(R.id.msg_left_boxchat);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView messageRight;
        private Context mContext;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            messageRight = itemView.findViewById(R.id.msg_right_boxchat);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //
        }
    }

    public void setData(List<DetailsChatModel> newData) {
        detailsChatModelList.clear();
        detailsChatModelList.addAll(newData);
        notifyDataSetChanged();
    }
}