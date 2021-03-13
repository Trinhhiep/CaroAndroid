package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.caroonline.models.Player;
import com.example.caroonline.models.Room;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RoomActivity extends AppCompatActivity {
    String roomId;
    RecyclerView recyclerView;
    Button btnPlay;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        if (intent.getStringExtra("RoomId") != null) {
            roomId = intent.getStringExtra("RoomId");
            setTitle(roomId);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.rcv_player);
        btnPlay = findViewById(R.id.btn_play);

        DatabaseReference myRef = FirebaseSingleton.getInstance().databaseReference.child("room").child(roomId).child("playerList");
        FirebaseRecyclerOptions<Player> options = new FirebaseRecyclerOptions.Builder<Player>()
                .setQuery(myRef, Player.class)
                .build();

        RecyclerPlayerAdapter adapter = new RecyclerPlayerAdapter(options);// bạn nói truyền vào chổ khởi tạo đây đúng ko
        adapter.startListening();// adapter kết nối layout item với data. mà bạn bảo là item  kì v.
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseSingleton.getInstance().databaseReference.child("room").child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room r = snapshot.getValue(Room.class);
                if (r != null) {
                    actionDependsOnStatus(r);
                    for (Player p : r.getPlayerList()
                    ) {
                        if (p.getName().compareTo(PlayerInfo.playerName) == 0 && p.isAdmin()) {
                            btnPlay.setVisibility(View.VISIBLE);

                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseSingleton.getInstance().databaseReference.child("room").child(roomId).child("status").setValue(Constraints.STATUS_PLAYING);
            }
        });


    }

    private void actionDependsOnStatus(Room r) {

        if (r.getStatus() == Constraints.STATUS_READY) {
            btnPlay.setEnabled(true);
        } else {
            btnPlay.setEnabled(false);

        }
        if (r.getStatus() == Constraints.STATUS_PLAYING) {
            startGameActivity();
        }

    }

    private void startGameActivity() {
        Intent intent = new Intent(RoomActivity.this, GameActivity.class);
        intent.putExtra("RoomId", roomId);

        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            removePlayer();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void removePlayer() {
        FirebaseSingleton.getInstance().remove(roomId, PlayerInfo.playerName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}