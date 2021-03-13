package com.example.caroonline;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caroonline.models.Node;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerNodeAdapter extends FirebaseRecyclerAdapter<Node, RecyclerNodeAdapter.NodeViewHolder> {
    private itemClickListener listener;
private Context context;
    public RecyclerNodeAdapter(@NonNull FirebaseRecyclerOptions<Node> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NodeViewHolder holder, int position, @NonNull Node model) {
        holder.iv_node = model.getImageView();
    }


    @NonNull
    @Override
    public NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_node, parent, false);
        return new NodeViewHolder(v);
    }


    private void addListener(itemClickListener listener) {
        this.listener = listener;
    }

    public class NodeViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_node;
        Node node;

        public void setIv_node(ImageView iv_node) {
            this.iv_node = iv_node;
        }

        public NodeViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_node = itemView.findViewById(R.id.iv_node);
            setLayoutItemView(iv_node);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(node);
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

    private interface itemClickListener {
        void onItemClick(Node node);
    }
}

