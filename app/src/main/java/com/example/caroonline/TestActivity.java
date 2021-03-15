package com.example.caroonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.caroonline.models.Node;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TestRecyclerAdapter adapter;
    List<Node> lst;
int [][] matrix=new int[20][20];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = findViewById(R.id.rcv_node1);

        lst = new ArrayList<>();

        for (int i = 1; i <= 400; i++) {
            lst.add(new Node(Constraints.IMAGE_ID_NULL)); // mới tạo thì đương nhiên nó rỗng r.

        }

        adapter = new TestRecyclerAdapter(lst, this);

        //bạn k đưa adapter vô recycler bạn lol của tôi ơi.
        recyclerView.setAdapter(adapter);// ua sao quen dua vao mà nay gio no van chay được vay,  thì bạn k đưa vô thì nó k làm gì thôi. sao có lỗi đc. từ từ fix dần nha.
        GridLayoutManager LayoutManager = new GridLayoutManager(this, Constraints.COLUMN_COUNT_ITEM_IMAGE); // de minh coi.
        recyclerView.setLayoutManager(LayoutManager);
        adapter.addItemClickListener(new TestRecyclerAdapter.ItemClickListener() {//cai này sao bn
            @Override
            public void onItemClick(int position) {
                lst.get(position).setImageId(Constraints.IMAGE_ID_O);
                adapter.notifyItemChanged(position);
                int x = position / Constraints.COLUMN_COUNT_ITEM_IMAGE;
                int y = position % Constraints.COLUMN_COUNT_ITEM_IMAGE;
                matrix[x][y] = 1;


                if( isEndGame(matrix,x,y,1)){
                    Toast.makeText(TestActivity.this,"Kết thúc game",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private boolean isEndGame(int[][] matrix, int x, int y, int value) {
        return isEndHorizontal(matrix, x, y, value) || isEndVertical(matrix, x, y, value) || isEndMain(matrix, x, y, value) || isEndSecondary(matrix, x, y, value);
    }


    private boolean isEndSecondary(int[][] matrix, int x, int y, int value) {
        int topRignt = 0;
        for (int i = 0; i <= x; i++) {
            if (x - i < 0 || y + i < Constraints.COLUMN_COUNT_ITEM_IMAGE) break;
            if (matrix[x - i][y + i] == value) {
                topRignt++;
            } else break;
        }

        int bottomLeft = 0;

        for (int i = 1; i <= Constraints.ROW_COUNT_ITEM_IMAGE - x; i++) {
            if (x + i > Constraints.ROW_COUNT_ITEM_IMAGE || y - i <0)
                break;
            if (matrix[x + i][y - i] == value) {
                bottomLeft++;
            } else break;
        }


        return topRignt + bottomLeft == 5;

    }

    private boolean isEndMain(int[][] matrix, int x, int y, int value) {
        int topLeft = 0;
        for (int i = 0; i <= x; i++) {
            if (x - i < 0 || y - i < 0) break;
            if (matrix[x - i][y - i] == value) {
                topLeft++;
            } else break;
        }

        int bottomRight = 0;

        for (int i = 1; i <= Constraints.ROW_COUNT_ITEM_IMAGE - x; i++) {
            if (x + i > Constraints.ROW_COUNT_ITEM_IMAGE || y + i > Constraints.COLUMN_COUNT_ITEM_IMAGE)
                break;
            if (matrix[x + i][y + i] == value) {
                bottomRight++;
            } else break;
        }


        return topLeft + bottomRight == 5;
    }

    private boolean isEndVertical(int[][] matrix, int x, int y, int value) {
        int top = 0;
        for (int i = x; i >= 0; i--) {
            if (matrix[i][y] == value) {
                top++;
            } else break;
        }
        //duyet ben phai
        int bottom = 0;
        for (int j = x + 1; j <= Constraints.ROW_COUNT_ITEM_IMAGE; j++) {
            if (matrix[j][y] == value) {
                bottom++;
            } else break;
        }

        return top + bottom == 5;

    }

    private boolean isEndHorizontal(int[][] matrix, int x, int y, int value) {
//duyệt ben trái
        int left = 0;
        for (int i = y; i >= 0; i--) {
            if (matrix[x][i] == value) {
                left++;
            } else break;
        }
        //duyet ben phai
        int right = 0;
        for (int j = y + 1; j <= Constraints.COLUMN_COUNT_ITEM_IMAGE; j++) {
            if (matrix[x][j] == value) {
                right++;
            } else break;
        }

        return left + right == 5;
    }

}