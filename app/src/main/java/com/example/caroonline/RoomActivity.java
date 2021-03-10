package com.example.caroonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.caroonline.models.Room;
import com.example.caroonline.models.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RoomActivity extends AppCompatActivity {
    String roomId;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        if (intent.getStringExtra("RoomId") != null)
            roomId = intent.getStringExtra("RoomId");

        recyclerView = findViewById(R.id.rcv_player);

        DatabaseReference myRef = FirebaseSingleton.getInstance().databaseReference.child("room").child(roomId).child("playerIdList");
        FirebaseRecyclerOptions<String> options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(myRef, String.class)
                .build();

        RecyclerPlayerAdapter adapter = new RecyclerPlayerAdapter(options);
        adapter.startListening();// adapter kết nối layout item với data. mà bạn bảo là item  kì v.
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}