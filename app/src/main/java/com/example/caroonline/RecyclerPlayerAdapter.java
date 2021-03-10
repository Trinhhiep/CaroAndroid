package com.example.caroonline;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class RecyclerPlayerAdapter extends FirebaseRecyclerAdapter<String, RecyclerPlayerAdapter.UserViewHolder> {

    public RecyclerPlayerAdapter(@NonNull FirebaseRecyclerOptions<String> options) {
        super(options);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_player, parent, false); // cai l gi day ban. bạn vẫn mơ hồ quá ta. kkk.

        return new UserViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull String model) {
        if (position == 0) {//cái này phải là position trong room.listIdPlayer
            holder.setAdmin(true);
        }
        if (holder.isAdmin)
        {
            holder.imageView.setVisibility(View.VISIBLE);
            if(holder.userName.toString().compareTo(MainActivity.usernameStatic)==0)
            holder.btnStart.setVisibility(View.VISIBLE);
        }
        if (model.compareTo(MainActivity.usernameStatic) == 0) {
            holder.setUserName(model, Color.parseColor("#ff008000"));
        } else
            holder.setUserName(model, Color.parseColor("#AAAAAA"));
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        boolean isAdmin = false;
        ImageView imageView;
Button btnStart;
        public void setUserName(String userName, int color) {
            this.userName.setText(userName);
            this.userName.setTextColor(color);

        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_userName);
            imageView = itemView.findViewById(R.id.im_ic_home);
            btnStart = itemView.findViewById(R.id.btn_start);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
