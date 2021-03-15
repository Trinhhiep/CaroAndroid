package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    String roomId;
    String adminId;
    int imageId;
    boolean isYourTurning = false;
    boolean doubleBackToExitPressedOnce = false; // cái này note sai ha
    int currentPlayer;// luu thang nao duoc danh tiep theo
    RecyclerView recyclerView;
    TextView tv_your_turn;
    TextView tv_other;
    Button btnNewGame;
    int[][] matrix = new int[Constraints.COUNT_ITEM_IMAGE / Constraints.COLUMN_COUNT_ITEM_IMAGE][Constraints.COLUMN_COUNT_ITEM_IMAGE];
    boolean hasEndGame = false;

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
            imageId = Integer.parseInt(info[1]); // ok chưa obkạn
            adminId = info[2];
            setTitle(roomId);
        }


        recyclerView = findViewById(R.id.rcv_node);
        tv_your_turn = findViewById(R.id.tv_yourturn);
        tv_other = findViewById(R.id.tv_orther);
        btnNewGame = findViewById(R.id.btn_newgame);
        // update UI : turn,...
        updateUI();

        // show chessboard
        DatabaseReference myRef = FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("listNode");
        FirebaseRecyclerOptions<Node> options = new FirebaseRecyclerOptions.Builder<Node>()
                .setQuery(myRef, Node.class)
                .build();
        RecyclerNodeAdapter adapter = new RecyclerNodeAdapter(options, this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, Constraints.COLUMN_COUNT_ITEM_IMAGE));

        // event click
        adapter.addItemClickListener(new RecyclerNodeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isYourTurning) {
                    int x = position / Constraints.COLUMN_COUNT_ITEM_IMAGE;
                    int y = position % Constraints.COLUMN_COUNT_ITEM_IMAGE;
                    if (matrix[x][y] == Constraints.IMAGE_ID_NULL) {
                        FirebaseSingleton.getInstance().insert(roomId, position, imageId);
                        FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("currentPlayer").setValue(changeCurrent(currentPlayer));
                    }
                }

            }
        });

        //getListNode kiểm tra thắng thua
        checkWin(roomId);

        // start new game
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasEndGame) {
                    Toast.makeText(GameActivity.this, "The game is not over", Toast.LENGTH_SHORT).show();
                }
                if (adminId.compareTo(PlayerInfo.playerName) != 0) { //sao no k loi cung hay day.dang le nó phai thong bao chu
                    Toast.makeText(GameActivity.this, "Only the admin can restart game ", Toast.LENGTH_SHORT).show();
                }
                if (hasEndGame && adminId.compareTo(PlayerInfo.playerName) == 0) {
                    restartGame();
                }

            }
        });
    }


    //
    private void checkWin(String roomId) {
        FirebaseSingleton.getInstance().databaseReference.child("game").child(roomId).child("listNode").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                int value = snapshot.getValue(Node.class).getImageId();
                int pos = Integer.parseInt(snapshot.getKey());
                int x = pos / Constraints.COLUMN_COUNT_ITEM_IMAGE;
                int y = pos % Constraints.COLUMN_COUNT_ITEM_IMAGE;
                matrix[x][y] = value;
                if (isEndGame(matrix, x, y, value)) {
                    Toast.makeText(GameActivity.this, "Kết thúc game", Toast.LENGTH_SHORT).show();
                    hasEndGame = true;
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean isEndGame(int[][] matrix, int x, int y, int imageId) {
        return isEndHorizontal(matrix, x, y, imageId) || isEndVertical(matrix, x, y, imageId) || isEndMain(matrix, x, y, imageId) || isEndSecondary(matrix, x, y, imageId);
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

    private boolean isEndSecondary(int[][] matrix, int x, int y, int imageId) {
        int endPoint = 0;
        int count = 0;
        int id;
        for (int i = 0; i <= 6; i++) {
            if (!isOnChessBoard(x - i, y + 1))
                break;
            id = matrix[x - i][y + i];
            if (id == imageId) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }
        }

        for (int i = 1; i <= 6; i++) {
            if (!isOnChessBoard(x + i, y - 1))
                break;
            id = matrix[x + i][y - i];
            if (id == imageId) {
                count++;
            } else {
                if (id != Constraints.IMAGE_ID_NULL)
                    endPoint++;
                break;
            }
        }

        return hasWin(count, endPoint); // may nghe t noi k co

    }


    private boolean isEndMain(int[][] matrix, int x, int y, int value) {
        int endPoint = 0;
        int count = 0;
        int id;
        for (int i = 0; i <= 6; i++) {
            if (!isOnChessBoard(x - i, y - i)) break;
            id = matrix[x - i][y - i];
            if (id == value) {
                count++;
            } else if (id != Constraints.IMAGE_ID_NULL)
                endPoint++;
            break;

        }


        for (int i = 1; i <= 6; i++) {
            if (!isOnChessBoard(x + i, y + i))
                break;
            id = matrix[x + i][y + i];
            if (id == value) {
                count++;
            } else if (id != Constraints.IMAGE_ID_NULL)
                endPoint++;
            break;

        }
        return hasWin(count, endPoint);

    }

    private boolean isEndVertical(int[][] matrix, int x, int y, int value) {
        int endPoint = 0;
        int count = 0;
        int id;
        for (int i = x; i >= 0; i--) {
            id = matrix[i][y];
            if (id == value) {
                count++;
            } else if (id == Constraints.IMAGE_ID_NULL)
                endPoint++;
            break;

        }
        for (int j = x + 1; j <= x + 1 + 6; j++) {
            id = matrix[j][y];
            if (id == value) {
                count++;
            } else if (id == Constraints.IMAGE_ID_NULL)

                endPoint++;
            break;

        }
        return hasWin(count, endPoint);


    }

    private boolean isEndHorizontal(int[][] matrix, int x, int y, int value) {
//duyệt ben trái
        int endPoint = 0;
        int count = 0;
        int id;
        for (int i = y; i >= 0; i--) {
            id = matrix[x][i];
            if (id == value) {
                count++;
            } else if (id == Constraints.IMAGE_ID_NULL)
                endPoint++;
            break;
        }

        for (int j = y + 1; j <= y + 1 + 6; j++) {
            id = matrix[x][j];
            if (id == value) {
                count++;
            } else if (id == Constraints.IMAGE_ID_NULL)

                endPoint++;
            break;

        }
        return hasWin(count, endPoint);
    }

    private int checkLaw(int x) {
        int note = 0;


        return note;
    }

    private void updateUI() {
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

    private void restartGame() {
        List<Node> lst = new ArrayList<>();
        for (int i = 1; i <= Constraints.COUNT_ITEM_IMAGE; i++) {
            lst.add(new Node(Constraints.IMAGE_ID_NULL)); // mới tạo thì đương nhiên nó rỗng r.
        }
        Game game = new Game(roomId, lst, Constraints.IMAGE_ID_O);// set cho thang chu danh truoc luon
        FirebaseSingleton.getInstance().insert(game);
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