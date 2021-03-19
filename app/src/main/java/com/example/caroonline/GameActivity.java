package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caroonline.models.Game;
import com.example.caroonline.models.Node;
import com.example.caroonline.models.Room;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    String roomId;
    String adminId;
    int imageId;
    boolean isYourTurning = false;
    boolean doubleBackToExitPressedOnce = false;
    int currentPlayer;
    RecyclerView recyclerView;
    TextView tv_your_turn;
    TextView tv_other;
    Button btnNewGame;
    RecyclerNodeAdapter adapter;
    // sao k dung list nay thoi.
    List<Node> listNode = new ArrayList<>();

    int status = Constraints.GAME_STATUS_PLAYING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //set title
        Intent intent = getIntent();
        if (intent.getStringArrayExtra("Info") != null) {
            String[] info = intent.getStringArrayExtra("Info");
            roomId = info[0];
            imageId = Integer.parseInt(info[1]);
            adminId = info[2];


            setTitle(roomId);
        }

        newGame();
        recyclerView = findViewById(R.id.rcv_node);
        tv_your_turn = findViewById(R.id.tv_yourturn);
        tv_other = findViewById(R.id.tv_orther);
        btnNewGame = findViewById(R.id.btn_newgame);

        // show chessboard

        adapter = new RecyclerNodeAdapter(this, listNode);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, Constraints.COLUMN_COUNT_ITEM_IMAGE));


        // event click
        adapter.addItemClickListener(new RecyclerNodeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (isYourTurning && status == Constraints.GAME_STATUS_PLAYING) {
                    int x = position / Constraints.COLUMN_COUNT_ITEM_IMAGE;
                    int y = position % Constraints.COLUMN_COUNT_ITEM_IMAGE;
                    if (listNode.get(position).getImageId() == Constraints.IMAGE_ID_NULL) { // ne ? cai gi nua ma ? lam chưa ok sao minh giám sửa
                        FirebaseSingleton.getInstance().insertNode(roomId, position, new Node(imageId));
                        FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("currentPlayer").setValue(changeCurrent(currentPlayer));
                    }
                }
            }
        });

        // update UI : turn,
        updateCurrentUI();
        updateChessBoardAndCheckWin(roomId);
        //theo doi trang thai cua game
        checkStatus(roomId);
        //kiem tra co ai thoat game giua chung
        listenPlayerOutGame(roomId);
        // start new game
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status == Constraints.GAME_STATUS_PLAYING) {
                    Toast.makeText(GameActivity.this, "The game is not over", Toast.LENGTH_SHORT).show();
                }
                if (adminId.compareTo(PlayerInfo.playerName) != 0) { //sao no k loi cung hay day.dang le nó phai thong bao chu
                    Toast.makeText(GameActivity.this, "Only the admin can restart game ", Toast.LENGTH_SHORT).show();
                }
                if (status == Constraints.GAME_STATUS_ENDED && adminId.compareTo(PlayerInfo.playerName) == 0) {
                    Toast.makeText(GameActivity.this, "Restart ", Toast.LENGTH_SHORT).show();

                    restartGame();
                }
            }
        });
    }

    private void checkStatus(String roomId) {
        FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int value = snapshot.getValue(int.class);
                status = value;
                if (status == Constraints.GAME_STATUS_NEWGAME) { // co cai nay sao ban k noi minh
                    listNode.clear();
                    //ar vay no ve 0 het chua.//sao lai 0
                    for (int i = 0; i < Constraints.ROW_COUNT_ITEM_IMAGE * Constraints.COLUMN_COUNT_ITEM_IMAGE; i++) {
                        listNode.add(new Node(Constraints.IMAGE_ID_NULL));
                    }
                    adapter.notifyDataSetChanged();

                    FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("status").setValue(Constraints.GAME_STATUS_PLAYING);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateChessBoardAndCheckWin(String roomId) {

        FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("listNode").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Node node = snapshot.getValue(Node.class);
                int pos = Integer.parseInt(snapshot.getKey());
                updateListNode(pos, node);
                int imageIdValue = node.getImageId();
                if (imageIdValue != Constraints.IMAGE_ID_NULL) {

                    if (isEndGame(pos, imageIdValue)) {
                        String mess;
                        if (imageId == imageIdValue)
                            mess = "ENDED GAME, YOU WIN";
                        else mess = "ENDED GAME, YOU LOSE";


                        FragmentManager fm = getSupportFragmentManager();
                        MessageEndedGameDialogFragment userInfoDialog = MessageEndedGameDialogFragment.newInstance(mess);
                        userInfoDialog.show(fm, null);
                        userInfoDialog.setCancelable(false);

                        FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("status").setValue(Constraints.GAME_STATUS_ENDED);

                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // new game co xoa list r.thi  ban coi xoa ở cho nao hợp lý thì làm , chứ minh biêt đâu
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateListNode(int pos, Node node) {
        listNode.get(pos).setImageId(node.getImageId());// ban co 1 list 400 phan tu r ban con muon add them nua har.
        adapter.notifyItemChanged(pos);

    }


    private void updateCurrentUI() {
        FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("currentPlayer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int current = snapshot.getValue(int.class);
                currentPlayer = current;
                setIsYourTurn(imageId, currentPlayer);
                updateUITurn(isYourTurning);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // k co ham set nao return het ban. ? sao
    private void setIsYourTurn(int imageId, int currentPlayer) {
        if (imageId != currentPlayer) {
            isYourTurning = false;
            return;
        }
        isYourTurning = true;
    }

    private void updateUITurn(boolean isYourTurn) {
        if (isYourTurn) {
            changeColorTextTurn(tv_your_turn, true);
            changeColorTextTurn(tv_other, false);

        } else {
            changeColorTextTurn(tv_your_turn, false);
            changeColorTextTurn(tv_other, true);
        }
    }

    private void changeColorTextTurn(TextView textView, boolean isTurn) {
        if (isTurn) {
            textView.setTextColor(Constraints.COLOR_BLACK);
            textView.setBackgroundColor(Constraints.COLOR_GREEN);
            return;
        }
        textView.setTextColor(Constraints.COLOR_BLACK);
        textView.setBackgroundColor(Constraints.COLOR_WHITE);
    }

    private int changeCurrent(int currentPlayer) {

        return currentPlayer == Constraints.IMAGE_ID_X ? Constraints.IMAGE_ID_O : Constraints.IMAGE_ID_X;
    }

    private void restartGame() {
        FirebaseSingleton.getInstance().restartGame(roomId); // doi status v ne no se vo trong check status
    }

    private void newGame() { // vai dat ten.DA NOI LA T MOI SUA CODE , THU CO.I  CHiAYo kodkocko kokokkoOko kokokokookok ok.

        listNode = new ArrayList<>();
        for (int i = 0; i < Constraints.ROW_COUNT_ITEM_IMAGE * Constraints.COLUMN_COUNT_ITEM_IMAGE; i++) {//  KHAC GI DAU BAN
            listNode.add(new Node(Constraints.IMAGE_ID_NULL)); /// sao add it v.
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    protected void onDestroy() {
//        removePlayer();
//
//        super.onDestroy();
//        finish();
//        return;
//    }


    private void startMenuRoomActivity() {
        Intent intent = new Intent(GameActivity.this, MenuRoomActivity.class);
//        intent.putExtra("RoomId", roomId);
        startActivity(intent);
    }

    private void startRoomActivity() {
        Intent intent = new Intent(GameActivity.this, RoomActivity.class);
        intent.putExtra("RoomId", roomId);
        startActivity(intent);
    }

    private void removePlayer() {
        FirebaseSingleton.getInstance().removePlayer(roomId, PlayerInfo.playerName);
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            removePlayer();
            super.onBackPressed();
//            finish();
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

    private void listenPlayerOutGame(String roomId) {
        FirebaseSingleton.getInstance().databaseReference.child("room").child(roomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room room = snapshot.getValue(Room.class);
                if (room != null) {
                    if (room.getAdmin().compareTo(PlayerInfo.playerName) == 0 && room.getOther().isEmpty()) {

                        Toast.makeText(GameActivity.this, "Your opponent surrenders, you win.", Toast.LENGTH_SHORT).show();
//                        finish();
                        startRoomActivity();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isEndGame(int pos, int imageId) {
        return isEndHorizontal(pos, imageId) || isEndVertical(pos, imageId) || isEndMain(pos, imageId) || isEndSecondary(pos, imageId);
    }

    private boolean hasWin(int nodeCount, int endPoint) {
        if (endPoint != 2)
            return nodeCount >= 5;
        else
            return false;
    }

    private boolean isOnChessBoard(int x, int y) {

        if (x >= Constraints.ROW_COUNT_ITEM_IMAGE || x < 0
                || y >= Constraints.COLUMN_COUNT_ITEM_IMAGE || y < 0) {
            return false;
        }
        return true;
    }

    private boolean isEndSecondary(int position, int imageId) {
        // lam mau voi th nay nha ok
        int endPoint = 0;
        int count = 0;
        int id;
        // day la not goc
        int x = position / Constraints.COLUMN_COUNT_ITEM_IMAGE;
        int y = position % Constraints.COLUMN_COUNT_ITEM_IMAGE;
        for (int i = 0; i <= 6; i++) {
            if (!isOnChessBoard(x - i, y + i))
                break;
            int pos = (x - i) * (Constraints.COLUMN_COUNT_ITEM_IMAGE) + (y + i);//day nè ban oi , ko có +1 nha
            id = listNode.get(pos).getImageId();
            if (id == imageId) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }
        }

        for (int i = 1; i <= 6; i++) {
            if (!isOnChessBoard(x + i, y - i))
                break;
            int pos = (x + i) * (Constraints.COLUMN_COUNT_ITEM_IMAGE) + (y - i);
            id = listNode.get(pos).getImageId();
            if (id == imageId) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }
        }

        return hasWin(count, endPoint);

    }


    private boolean isEndMain(int position, int value) {
        int endPoint = 0;
        int count = 0;
        int id;

        int x = position / Constraints.COLUMN_COUNT_ITEM_IMAGE;
        int y = position % Constraints.COLUMN_COUNT_ITEM_IMAGE;
        for (int i = 0; i <= 6; i++) {
            if (!isOnChessBoard(x - i, y - i))
                break;
            int pos = (x - i) * (Constraints.COLUMN_COUNT_ITEM_IMAGE) + (y - i);
            id = listNode.get(pos).getImageId();
            if (id == value) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }
        }

        for (int i = 1; i <= 6; i++) {
            if (!isOnChessBoard(x + i, y + i))
                break;
            int pos = (x + i) * (Constraints.COLUMN_COUNT_ITEM_IMAGE) + (y + i);
            id = listNode.get(pos).getImageId();
            if (id == value) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }
        }
        return hasWin(count, endPoint);
    }

    private boolean isEndVertical(int position, int value) {
        int endPoint = 0;
        int count = 0;
        int id;


        int x = position / Constraints.COLUMN_COUNT_ITEM_IMAGE;
        int y = position % Constraints.COLUMN_COUNT_ITEM_IMAGE;
        for (int i = x; i >= x - 6; i--) { // cai nay nhin no sao sao ay ban. alo
            if (!isOnChessBoard(i, y)) break;
            int pos = (i) * (Constraints.COLUMN_COUNT_ITEM_IMAGE) + (y);
            id = listNode.get(pos).getImageId();
            if (id == value) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }
        }


        for (int j = x + 1; j <= x + 1 + 6; j++) {  // vl
            if (!isOnChessBoard(j, y)) break;
            int pos = (j) * (Constraints.COLUMN_COUNT_ITEM_IMAGE) + (y);
            id = listNode.get(pos).getImageId();
            if (id == value) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }
        }
        return hasWin(count, endPoint);
    }

    private boolean isEndHorizontal(int position, int value) {
        int endPoint = 0;
        int count = 0;
        int id;
        int x = position / Constraints.COLUMN_COUNT_ITEM_IMAGE;
        int y = position % Constraints.COLUMN_COUNT_ITEM_IMAGE;
        for (int i = y; i >= y - 6; i--) {
            if (!isOnChessBoard(x, i)) break;
            int pos = (x) * (Constraints.COLUMN_COUNT_ITEM_IMAGE) + (i);

            id = listNode.get(pos).getImageId();
            if (id == value) { // nè. code ban đầu là vầy.
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break; //ok r. vua r ban de break o ngoai thi no chi chay for co 1 lan thoi.ok
            }
        }

        for (int j = y + 1; j <= y + 1 + 6; j++) {
            if (!isOnChessBoard(x, j)) break;
            int pos = (x) * (Constraints.COLUMN_COUNT_ITEM_IMAGE) + (j);

            id = listNode.get(pos).getImageId();
            if (id == value) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }

        }
        return hasWin(count, endPoint);
    }

}