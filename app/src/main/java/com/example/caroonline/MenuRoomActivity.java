package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.caroonline.models.Player;
import com.example.caroonline.models.Room;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MenuRoomActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton flab;
    Button btnLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_room);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String title = String.format("Hello %s", PlayerInfo.playerName);
        setTitle(title);

        recyclerView = findViewById(R.id.rv_room);
        flab = findViewById(R.id.flab);
        btnLogOut = findViewById(R.id.btn_logout);
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
                if (room.couldAddPlayer()) {
                    FirebaseSingleton.getInstance().addPlayer(room.getId(), PlayerInfo.playerName); // add thang khach nha ban.
                    startRoomActivity(room.getId());
                } else
                    Toast.makeText(MenuRoomActivity.this, "Room is full", Toast.LENGTH_SHORT).show();
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuRoomActivity.this, MainActivity.class));
            }
        });
        flab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CreateNewRoomDialogFragment userInfoDialog = CreateNewRoomDialogFragment.newInstance();
                userInfoDialog.show(fm, null);
                userInfoDialog.setCancelable(false); // cai nay k cho huy dialog khi an vo 1 v??ng khac.oki
                userInfoDialog.addCreateRoomListener(new CreateRoomListener() {
                    @Override
                    public void onCreateRoom(String roomName) {
                        Room room = new Room(roomName, PlayerInfo.playerName);
                        FirebaseSingleton.getInstance().insert(room);

                        userInfoDialog.dismiss();

                        startRoomActivity(room.getId());
                    }
                });
            }
        });
    }

    //  gi day mi. mi hoc dau  ??.
//cai nao   ham nay ne.c??i n??y h??nh nhu l?? luu tr???ng th??i ???? b???n
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("PlayerInfo", PlayerInfo.playerName);// c??i n??y m??nh nh??? l?? b???n vi???t m??. lam gi co.
    }

    private void startRoomActivity(String roomId) {
        Intent intent = new Intent(MenuRoomActivity.this, RoomActivity.class);
        intent.putExtra("RoomId", roomId);

        startActivity(intent);
    }

    public void onBackPressed() {

    }

    //  xong menu room activity nha ban.

}