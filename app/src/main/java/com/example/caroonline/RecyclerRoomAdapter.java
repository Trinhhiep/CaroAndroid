package com.example.caroonline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caroonline.models.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerRoomAdapter extends FirebaseRecyclerAdapter<Room, RecyclerRoomAdapter.RoomViewHolder> {

    private ItemClickListener listener;

    public RecyclerRoomAdapter(@NonNull FirebaseRecyclerOptions<Room> options) {
        super(options);
    }
    // sao bạn đ*o  nói  sớm nhung cái room cung phai sua ma, minh tuong ban sua list room truoc

    @Override
    protected void onBindViewHolder(@NonNull RoomViewHolder holder, int position, @NonNull Room model) {
        holder.setName(model.getName());
        holder.setStatus(model.getStatus());
        holder.setPlayerCount(model.getPlayerCount());
        holder.setMaxPlayerCount(Constraints.MAX_PLAYER_COUNT);
        holder.setRoom(model);
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(v);
    }

    public void addItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView status;
        TextView playerCount;

        TextView maxPlayerCount;
        Room room;


        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_room_name);
            status = itemView.findViewById(R.id.tv_status);
            playerCount = itemView.findViewById(R.id.tv_player_count);
            maxPlayerCount = itemView.findViewById(R.id.tv_max_player_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(room);
                }
            });

        }


        public void setName(String name) {
            this.name.setText(name);
        }


        public void setStatus(int status) {
            this.status.setText(Constraints.roomStatus(status));
        }


        public void setPlayerCount(int playerCount) {
            this.playerCount.setText(Integer.toString(playerCount));
        }


        public void setMaxPlayerCount(int maxPlayerCount) {
            this.maxPlayerCount.setText(Integer.toString(maxPlayerCount));
        }


        public void setRoom(Room room) {
            this.room = room;
        }
    }

    public interface ItemClickListener {
        void onItemClick(Room room);
    }
}

