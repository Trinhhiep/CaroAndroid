package com.example.caroonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.caroonline.models.Game;
import com.example.caroonline.models.Node;
import com.example.caroonline.models.Room;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    String roomId;
    RecyclerView recyclerView;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent.getStringExtra("RoomId") != null) {
            roomId = intent.getStringExtra("RoomId");
            setTitle(roomId);
        }
        recyclerView=findViewById(R.id.rcv_node);

        Game game = new Game(roomId,new ArrayList<Node>());
        List<Node>  lst= new ArrayList<Node>();
        for(int i=0 ;i<=99;i++){
            lst.add(new Node("",null,null));
        }
        game.setListNode(lst);
        FirebaseSingleton.getInstance().insert(game);
        DatabaseReference myRef = FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("listNode");
        FirebaseRecyclerOptions<Node> options = new FirebaseRecyclerOptions.Builder<Node>()
                .setQuery(myRef, Node.class)
                .build();
        RecyclerNodeAdapter adapter = new RecyclerNodeAdapter(options, this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,10));


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            removePlayer();

            super.onBackPressed();
            finish();
            startMenuRoomActivity();



            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "If you exit the game, you lose.\n Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void startMenuRoomActivity() {
        Intent intent = new Intent(GameActivity.this, MenuRoomActivity.class);
        intent.putExtra("RoomId", roomId);
        startActivity(intent);
    }

    private void removePlayer() {
        FirebaseSingleton.getInstance().remove(roomId, PlayerInfo.playerName);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}