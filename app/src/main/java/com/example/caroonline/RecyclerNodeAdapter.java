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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerNodeAdapter extends FirebaseRecyclerAdapter<Node, RecyclerNodeAdapter.NodeViewHolder> {
    private ItemClickListener listener;
    private Context context;

    public RecyclerNodeAdapter(@NonNull FirebaseRecyclerOptions<Node> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NodeViewHolder holder, int position, @NonNull Node model) {
        if (model.getImageId() == Constraints.IMAGE_ID_NULL) { // test. tututtu
            if(holder.iv_node.getBackground() != null)
                holder.iv_node.setBackground(null);
            return;
        } else if (model.getImageId() == Constraints.IMAGE_ID_O) {
            Glide.with(context)
                    .load(R.drawable.ic_o)
                    .error(R.drawable.ic_o)
                    .fitCenter()
                    .into(holder.iv_node);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_x)
                    .error(R.drawable.ic_x)
                    .fitCenter()
                    .into(holder.iv_node);

        }
    }


    @NonNull
    @Override
    public NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_node, parent, false);
        return new NodeViewHolder(v);
    }


    public void addItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public class NodeViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_node;

        public NodeViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_node = itemView.findViewById(R.id.iv_node);
            setLayoutItemView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(getAdapterPosition());
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
        void onItemClick(int position);
    }
}

