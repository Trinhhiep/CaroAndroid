package com.example.caroonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.caroonline.models.Node;

import java.util.List;


public class TestRecyclerAdapter extends RecyclerView.Adapter<TestRecyclerAdapter.TestViewHolder> {
    private List<Node> listNode;
    private Context context;
    private ItemClickListener listener;

    public TestRecyclerAdapter(List<Node> listNode, Context Context) { // vay mà sao nay gio no van cahy , java si da nay ko biet bao lổi gi het, ban oi

        this.listNode = listNode;
        this.context = Context;
    }


    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_node, parent, false);
        return new TestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Node node = (Node) listNode.get(position);
        if (node.getImageId() == Constraints.IMAGE_ID_NULL) // cái này ddeer nó rỗng ok
            return;
        if(holder.iv_node.getBackground()==null){
            if (node.getImageId() == Constraints.IMAGE_ID_O) {// vầy cho clean nha bạn.ok
                //tip bn oi. r giờ bạn coi lại cái logic mình nói nãy giờ. sau khi thông não r thì làm cái online đi.// ko có tao do thi lam sao bn
                Glide.with(context)
                        .load(R.drawable.ic_o)
                        .error(R.drawable.ic_o)
                        .fitCenter()
                        .into(holder.iv_node);
                holder.iv_node.setClickable(false);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_x)
                        .error(R.drawable.ic_x)
                        .fitCenter()
                        .into(holder.iv_node);
holder.iv_node.setClickable(false);
            }
        }else {
            return;
        }

    }

    @Override
    public int getItemCount() {
        return listNode.size();

    }

    public void addItemClickListener(ItemClickListener listener) { // vua r ban chua set dc listener a ban. ban ngao da a. tham so dua vo nhung lại gan listener băng chính nó.ok
        this.listener = listener;
    }

    class TestViewHolder extends RecyclerView.ViewHolder { // bạn thấy view chưa.. // từ bài toán mà ra cả: bạn muốn hiển thị node đánh cảu người chơi thì bạn cần gì. 1 cái image đúng k.uk
        ImageView iv_node; // gồm 1 cái image hiển thị node nè.ok

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_node = itemView.findViewById(R.id.iv_node);
            setLayoutItemView(itemView); // cái lol má bạn.? cai nay // bạn k biết là set cái gì luôn.//

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(getAdapterPosition()); // giờ xử lí tiếp nha. xong cái bàn r đó. // nó chay dung dau ban. nó hiện các ô đẹp đẽ r kìa ban.
                }
            });
        }
    }

    private void setLayoutItemView(View itemView) {
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = Utility.widthOfImage(context);
        layoutParams.height = Utility.widthOfImage(context);
        itemView.setLayoutParams(layoutParams);
    }

    public interface ItemClickListener {
        void onItemClick(int pos);
    }

}
