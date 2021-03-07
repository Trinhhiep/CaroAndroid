package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caroonline.models.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MenuRoomActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_room);

        Intent intent = getIntent();
        if (intent.getStringExtra("username") != null) {
            String title = String.format("Hello %s", intent.getStringExtra("username"));
            setTitle(title); // ban coi lai cai item_room sau nha.oki  design coi sau het cug dc
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.rv_room); // ok ve may cai setup chua ban//Utility là gi do

        DatabaseReference myRef = FirebaseSingleton.getInstance().databaseReference.child("room");
        FirebaseRecyclerOptions<Room> options = new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(myRef, Room.class)
                .build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Room, RoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RoomViewHolder holder, int position, @NonNull Room model) {
                holder.setName(model.getName());
                holder.setStatus(model.getStatus());
                holder.setPlayerCount(model.getPlayerCount());
                holder.setMaxPlayerCount(model.getMaxPlayerCount());
            }

            @NonNull
            @Override
            public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View v = inflater.inflate(R.layout.item_room, parent, false);
                return new RoomViewHolder(v);
            }
        };
        adapter.startListening();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView status;
        TextView playerCount;
        TextView maxPlayerCount;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_room_name);
            status = itemView.findViewById(R.id.tv_status);
            playerCount = itemView.findViewById(R.id.tv_player_count);
            maxPlayerCount = itemView.findViewById(R.id.tv_max_player_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplication(), Integer.toString(getAdapterPosition()), Toast.LENGTH_SHORT).show();
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
    }
}