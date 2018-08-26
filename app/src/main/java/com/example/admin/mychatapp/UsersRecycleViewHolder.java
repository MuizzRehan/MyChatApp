package com.example.admin.mychatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecycleViewHolder extends RecyclerView.ViewHolder {
    View userView;

    public UsersRecycleViewHolder(@NonNull View itemView) {
        super(itemView);

        userView = itemView;
    }

    public void setStatus(String status) {
        TextView userStatus = userView.findViewById(R.id.user_status);
        userStatus.setText(status);
    }

    public void setName(String name) {
        TextView userName = userView.findViewById(R.id.user_name);
        userName.setText(name);
    }

    public void setImage(String thumbImage) {
        CircleImageView userImage = userView.findViewById(R.id.user_image);
        Picasso.get().load(thumbImage).placeholder(R.drawable.avatar_image).into(userImage);
        int a=0;
    }
}
