package com.example.caroonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.caroonline.models.Room;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;


public class MenuRoomActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton flab;
    String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_room);

        Intent intent = getIntent();
        if (intent.getStringExtra("username") != null) {
            playerName = intent.getStringExtra("username");
            String title = String.format("Hello %s", playerName);
            setTitle(title); // ban coi lai cai item_room sau nha.oki  design coi sau het cug dc
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.rv_room); // ok ve may cai setup chua ban//Utility là gi do
        flab = findViewById(R.id.flab);

        DatabaseReference myRef = FirebaseSingleton.getInstance().databaseReference.child("room");
        FirebaseRecyclerOptions<Room> options = new FirebaseRecyclerOptions.Builder<Room>()
                .setQuery(myRef, Room.class)
                .build();
        RecyclerRoomAdapter adapter = new RecyclerRoomAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.addItemClickListener(new RecyclerRoomAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Room room) {
//                FirebaseSingleton.getInstance().updateListPlayer(MainActivity.usernameStatic,room);
                startRoomActivity(room.getId());
            }
        });

        flab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateNewRoomDialogFragment userInfoDialog = CreateNewRoomDialogFragment.newInstance();
                userInfoDialog.show(fm, null);
                userInfoDialog.setCancelable(false); // cai nay k cho huy dialog khi an vo 1 vùng khac.oki
                userInfoDialog.addCreateRoomListener(new CreateRoomListener() {
                    @Override
                    public void onCreateRoom(String name) {
                        Room room = new Room(name, 2);
                        room.addPlayer(playerName);
                        FirebaseSingleton.getInstance().insert(room);
                        //insertRoom(name);

                        userInfoDialog.dismiss();

                        startRoomActivity(room.getId());
                    }
                });
            }
        });
    }

    private void startRoomActivity(String roomId) {
        Intent intent = new Intent(MenuRoomActivity.this, RoomActivity.class);
        intent.putExtra("RoomId", roomId);
        startActivity(intent);
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