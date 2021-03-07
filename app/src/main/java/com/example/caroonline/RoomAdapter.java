package com.example.caroonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caroonline.models.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomHolder> {
    public List<Room> roomList;
    public Context context;

    public RoomAdapter(List<Room> rooms, Context context) {
        this.roomList = rooms;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_room, parent, false);
        return new RoomHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHolder holder, int position) {
        Room room = roomList.get(position);
        holder.setName(room.getName());
        holder.setStatus(room.getStatus());
        holder.setMaxPlayerCount(room.getMaxPlayerCount());
holder.setPlayerCount((room.getPlayerCount()));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class RoomHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView status;
TextView maxPlayerCount;
        TextView playerCount;

        public RoomHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.tv_status);
            name = itemView.findViewById(R.id.tv_room_name);
            maxPlayerCount= itemView.findViewById(R.id.tv_max_player_count);
            playerCount = itemView.findViewById(R.id.tv_player_count);
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setStatus(int status) {
            this.status.setText(Constraints.roomStatus(status));
        }
        public  void setMaxPlayerCount(int max){
            this.maxPlayerCount.setText(Integer.toString(max));
        }
        public  void setPlayerCount(int count){
            this.playerCount.setText(Integer.toString(count));
        }


    }
}
