package com.example.caroonline;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    String roomId;
    int imageId;
    RecyclerView recyclerView;
    boolean isTurn= false;
    boolean doubleBackToExitPressedOnce = false; // giờ mình sẽ tạo 1 biến để ghi nhớ node cần đánh.
    int currentPlayer;// luu thang nao duoc danh tiep theo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent.getStringArrayExtra("Info") != null) {
            String[] info = intent.getStringArrayExtra("Info");
            roomId = info[0];
            imageId = Integer.parseInt(info[1]); // ok chưa obkạn
            setTitle(roomId);
        }

        // lấy current vè
        FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("currentPlayer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int current = snapshot.getValue(int.class);
                currentPlayer = current;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (imageId != currentPlayer) {
           isTurn = false;
            //khoa lai
        } else {
        isTurn=true;
            //mo ra
        }
        recyclerView = findViewById(R.id.rcv_node);




        DatabaseReference myRef = FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("listNode");
        FirebaseRecyclerOptions<Node> options = new FirebaseRecyclerOptions.Builder<Node>()
                .setQuery(myRef, Node.class)
                .build();
        RecyclerNodeAdapter adapter = new RecyclerNodeAdapter(options, this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, Constraints.SPAN_COUNT_ITEM_IMAGE));



        adapter.addItemClickListener(new RecyclerNodeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if(isTurn) {
                    FirebaseSingleton.getInstance().insert(roomId, position, imageId);
                    FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("currentPlayer").setValue(changeCurrent(currentPlayer));
                }

            }
        });
    }
private int changeCurrent(int currentPlayer)
{
    if (currentPlayer == Constraints.IMAGE_ID_X)
        return Constraints.IMAGE_ID_O;
    else
        return Constraints.IMAGE_ID_X;
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