package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.caroonline.models.Game;
import com.example.caroonline.models.Node;
import com.example.caroonline.models.Player;
import com.example.caroonline.models.Room;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {
    String roomId;
    RecyclerView recyclerView;
    RecyclerPlayerAdapter adapter;
    Button btnPlay;
    boolean doubleBackToExitPressedOnce = false;
    int imageId = Constraints.IMAGE_ID_O;// mặc định là O; llonj bạn ơi. phải từ đây chứ. ok chưa.ok
    String adminId = "";
    List<Player> playerList = new ArrayList<>(); // bạn nhìn coi có khi nào lưu k. ok

    // thằng admin start acitvity này thì  nó có list rỗng đúng k.uk
    // thằng khách start activity này thì nó có list rỗng đúng k.dung

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

        adapter = new RecyclerPlayerAdapter(this, playerList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // bthi ben day cung la player join voi player out. ben kia chi la 1 cai thoi. ban xu lis ngon lanh out o day da ne. tai khi out game la out room ma.
        //  can theo doi: so luong player, chi theo doi so luong player thoi ha.~, theo doi status nua.

        //  theo doi status
        FirebaseSingleton.getInstance().databaseReference.child("room").child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room room = snapshot.getValue(Room.class);
// có phải khi 1 thang nào đo..1......... chờ mình xíu.
                updatePlayerList(room.getAdmin(), room.getOther());
                adminId = room.getAdmin();
                actionDependsOnStatus(room.getStatus());
                if (room.getAdmin().compareTo(PlayerInfo.playerName) == 0) {
                    btnPlay.setVisibility(View.VISIBLE);

                } else btnPlay.setVisibility(View.INVISIBLE);

                // kiem tra co the play  dc  k  de cho enable ne.
                if (!room.couldAddPlayer())
                    btnPlay.setEnabled(true);
                else btnPlay.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //  gio  ban  thoeo doi de update UI   ne.ok . ban oi ?ca/i? nay gio minh theo doi ca room dc r ne.

        btnPlay.setOnClickListener(new View.OnClickListener() { // ghi nhấn play thì cho tạo game luôn nha bạn.chi, thì tạo game đó bạn. vô trong kia load liíist node thôi. game đc tạo trc.oki
            @Override
            public void onClick(View v) {
                // thằng start thì cho nó khác đi nha.
                imageId = Constraints.IMAGE_ID_X;
                createGame(roomId);
                FirebaseSingleton.getInstance().databaseReference.child("room").child(roomId).child("status").setValue(Constraints.STATUS_PLAYING);
            }
        });

    }

    private void updatePlayerList(String admin, String other) {//ok chua ban
        if (playerList.isEmpty()) {
            playerList.add(new Player(admin, true));
            if (!other.isEmpty()) {
                playerList.add(new Player(other, false));
            }
        }
        // khi  list bằng 1 thì đúng là chỉ có bên admin xử lí thôi.
        else if (playerList.size() == 1) {
            playerList.add(new Player(other, false));
        } else if (playerList.size() == 2) {
            if (other.isEmpty()) {//lúc 1 thằng bât kì thoat thì sao ban, thì đó bạn. khi 1 thằng thoát
                Player player = playerList.get(0);
                player.setName(admin);
                player.setAdmin(true);
                playerList.remove(1);
            }// mảng 2 phần tử remove index 2 là ok r á. bạn. k sửa à.ủa , khuủaa cái gì. lúc playlist =2 có 1 thang vọ nữa là 3 thang ở đâu ra 3.
        }

        // tại cái  này mình tự xử lí nên phải thông báo thay đổi ở adpater.
        adapter.notifyDataSetChanged();
    }

    private void createGame(String roomId) {
        List<Node> lst = new ArrayList<>();
        for (int i = 1; i <= Constraints.COUNT_ITEM_IMAGE; i++) {
            lst.add(new Node(Constraints.IMAGE_ID_NULL)); // mới tạo thì đương nhiên nó rỗng r.
        }
        Game game = new Game(roomId, lst, Constraints.IMAGE_ID_O,false);// set cho thang chu danh truoc luon
        FirebaseSingleton.getInstance().insert(game);
    }

    private void actionDependsOnStatus(int status) {
        if (status == Constraints.STATUS_READY) {
            btnPlay.setEnabled(true);
        } else {
            btnPlay.setEnabled(false);
        }
        if (status == Constraints.STATUS_PLAYING) {
            startGameActivity();
        }
    }

    private void startGameActivity() {
        Intent intent = new Intent(RoomActivity.this, GameActivity.class);
        String[] info = {roomId, Integer.toString(imageId), adminId}; // neu la khach thi admin van chua co gi.
        intent.putExtra("Info", info);//bo cai nay
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
        FirebaseSingleton.getInstance().removePlayer(roomId, PlayerInfo.playerName);
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