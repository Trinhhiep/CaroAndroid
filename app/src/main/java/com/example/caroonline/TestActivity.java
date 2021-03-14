package com.example.caroonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.caroonline.models.Node;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TestRecyclerAdapter adapter;
    List<Node> lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = findViewById(R.id.rcv_node1);

        lst = new ArrayList<>();

        for (int i = 1; i <= 300; i++) {
            lst.add(new Node(Constraints.IMAGE_ID_NULL)); // mới tạo thì đương nhiên nó rỗng r.
        }

        adapter = new TestRecyclerAdapter(lst, this);

        //bạn k đưa adapter vô recycler bạn lol của tôi ơi.
        recyclerView.setAdapter(adapter);// ua sao quen dua vao mà nay gio no van chay được vay,  thì bạn k đưa vô thì nó k làm gì thôi. sao có lỗi đc. từ từ fix dần nha.
        GridLayoutManager LayoutManager = new GridLayoutManager(this, Constraints.SPAN_COUNT_ITEM_IMAGE); // de minh coi.
        recyclerView.setLayoutManager(LayoutManager);
        adapter.addItemClickListener(new TestRecyclerAdapter.ItemClickListener() {//cai này sao bn
            @Override
            public void onItemClick(int position) {
                lst.get(position).setImageId(Constraints.IMAGE_ID_O);
                adapter.notifyItemChanged(position);
            }
        });
    }
}