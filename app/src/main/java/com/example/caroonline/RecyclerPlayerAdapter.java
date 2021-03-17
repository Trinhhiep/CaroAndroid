package com.example.caroonline;

import android.content.Context;
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

import com.example.caroonline.models.Player;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class RecyclerPlayerAdapter extends RecyclerView.Adapter<RecyclerPlayerAdapter.UserViewHolder> {

    private Context context;
    private List<Player> players;

    public RecyclerPlayerAdapter(Context context, List<Player> players) {
        this.context = context;
        this.players = players;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_player, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Player player  = players.get(position);
        holder.setUserName(player.getName());

        int color  = Constraints.COLOR_BLACK;
        if(player.getName().compareTo(PlayerInfo.playerName) == 0)
            color  = Constraints.PLAYER_COLOR;
        holder.setColor(color);

        holder.setVisibility(player.isAdmin());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView imageView;


        public void setUserName(String userName) {
            this.userName.setText(userName);
        }

        public void setColor(int playerColor) {
            this.userName.setTextColor(playerColor);
        }

        public void setVisibility(boolean isAdmin) {
            if (isAdmin) {
                this.imageView.setVisibility(View.VISIBLE);

            } else this.imageView.setVisibility(View.INVISIBLE);
        }

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.tv_userName);
            imageView = itemView.findViewById(R.id.im_ic_home);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    // xong adapter.  tu list  player hien  bt thoi.


}
