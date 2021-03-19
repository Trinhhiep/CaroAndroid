package com.example.caroonline;

import androidx.annotation.NonNull;

import com.example.caroonline.models.Game;
import com.example.caroonline.models.Node;
import com.example.caroonline.models.Room;
import com.example.caroonline.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseSingleton {
    public DatabaseReference databaseReference;

    //private constructor ko cho phép tạo instance ben ngoài class
    private FirebaseSingleton() { // day la singleton ha Hip// dung r ban // //hnaoc  khac gi static dau
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void insert(Room room) {// bef
        databaseReference.child("room").child(room.getId()).setValue(room);
    }

    public void removeRoomAndGame(String roomId) {
        databaseReference.child("room").child(roomId).setValue(null);
        databaseReference.child("game").child(roomId).setValue(null);

    }

    public void insert(User user) {
        databaseReference.child("user").child(user.getUsername()).setValue(user);
    }

    public void insert(Game game) {
        databaseReference.child("game").child(game.getRoomId()).setValue(game);
    }

    // ban bi gi v. restart game ne.
    public void restartGame(String roomId){
        // lay duong dan game ra nha ban. ban dau r ban lol......ok
        DatabaseReference game = databaseReference.child("game").child(roomId);
        // clear node
        game.child("listNode").setValue(null); // ne
        // set status, ban mo ta trang thai nay coi.// theo ten đó , chưa xong gaem , da xong game , newgame là luc thang chu bam nút tao game moi not ended
        game.child("status").setValue(Constraints.GAME_STATUS_NEWGAME);
        // current khoi set. dang luot thang nao de nguyen luôn cho nó công bằng.o7
    }

    // nay la add node nhi. run lau ha
    public void insertNode(String roomId, int position, Node node) {
        databaseReference.child("game").child(roomId).child("listNode").child(Integer.toString(position)).setValue(node); //hihi quen bạn ơi.// dể mình test đã chứ.ok
    }

    public void addPlayer(String roomId, String playerName) {
        databaseReference.child("room").child(roomId).child("other").setValue(playerName);
    }

    public void removePlayer(String roomId, String playerName) {

        databaseReference.child("room").child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Room room = snapshot.getValue(Room.class);
                room.remove(playerName);
                if (room.couldDestroy())
                {removeRoomAndGame(roomId);
                return;
                }
                else insert(room);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //tạo 1 instance duy nhất (static) thông qua nested class lớp lồng lớp
    // Khi Singleton được tải vào bộ nhớ thì SingletonHelper chưa được tải vào.
// Nó chỉ được tải khi và chỉ khi phương thức getInstance() được gọi.
// Với cách này tránh được lỗi cơ chế khởi tạo instance của Singleton trong Multi-Thread,
// performance cao do tách biệt được quá trình xử lý.
// Do đó, cách làm này được đánh giá là cách triển khai Singleton nhanh và hiệu quả nhất.
    private static class FirebaseSingletonHelper {
        private static final FirebaseSingleton Instance = new FirebaseSingleton();// có instance ne
    }

    // method để có thể truy xuất được thể hiện duy nhất đó mọi lúc mọi nơi trong chương trình.
    public static FirebaseSingleton getInstance() {
        return FirebaseSingletonHelper.Instance;
    }


}

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("room"); // r
//        myRef.child(room.getId()).setValue(room);
//        myRef.child(room.getId()).child("id").setValue("Hipml");