package com.example.prmfinal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<CartItem> orderItems;

    public OrderItemAdapter(List<CartItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        CartItem orderItem = orderItems.get(position);
        holder.tvItemName.setText(orderItem.getProductName());
        holder.tvItemQuantity.setText("Quantity: " + orderItem.getQuantity());
        holder.tvItemPrice.setText("Price: $" + orderItem.getPrice());

        Glide.with(holder.itemImage.getContext())
                .load(orderItem.getImageUrl())
                .placeholder(R.drawable.image1)
                .error(R.drawable.images)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemQuantity, tvItemPrice;
        ImageView itemImage;

        public OrderItemViewHolder(View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}