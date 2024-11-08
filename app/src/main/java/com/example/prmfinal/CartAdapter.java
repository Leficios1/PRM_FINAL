package com.example.prmfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemUpdateListener listener;

    public interface OnCartItemUpdateListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemDeleted(CartItem item);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemUpdateListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_ite, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Load product image
        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.image1)
                .error(R.drawable.images)
                .into(holder.productImage);

        // Set product name
        holder.productName.setText(item.getProductName());

        // Format and set price
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(item.getPrice()) + " ₫";
        holder.productPrice.setText(formattedPrice);

        // Set quantity
        holder.quantityText.setText(String.valueOf(item.getQuantity()));

        // Handle decrease button
        holder.decreaseButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                listener.onQuantityChanged(item, item.getQuantity() - 1);
            }
        });

        // Handle increase button
        holder.increaseButton.setOnClickListener(v -> {
            listener.onQuantityChanged(item, item.getQuantity() + 1);
        });

        // Handle delete button
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                    .setPositiveButton("Xóa", (dialog, which) -> listener.onItemDeleted(item))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView quantityText;
        ImageButton decreaseButton;
        ImageButton increaseButton;
        ImageButton deleteButton;

        CartViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityText = itemView.findViewById(R.id.quantityText);
            decreaseButton = itemView.findViewById(R.id.decreaseButton);
            increaseButton = itemView.findViewById(R.id.increaseButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public void updateItems(List<CartItem> newItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newItems);
        notifyDataSetChanged();
    }
}
