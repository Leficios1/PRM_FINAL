package com.example.prmfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OrderHistoryAdapter extends ArrayAdapter<OrderResponseDTO> {

    public OrderHistoryAdapter(Context context, List<OrderResponseDTO> orders) {
        super(context, 0, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_history_item, parent, false);
        }

        OrderResponseDTO order = getItem(position);

        TextView tvOrderNumber = convertView.findViewById(R.id.tvOrderNumber);
        TextView tvOrderDate = convertView.findViewById(R.id.tvOrderDate);
        TextView tvOrderAmount = convertView.findViewById(R.id.tvOrderAmount);
        TextView tvOrderStatus = convertView.findViewById(R.id.tvOrderStatus);
        Button btnOrderDetails = convertView.findViewById(R.id.btnOrderDetails);

        tvOrderNumber.setText("Order #" + order.getOrderId());
        tvOrderDate.setText("Order Date: " + order.getOrderDate().toString()); // Format as needed
        tvOrderAmount.setText("$" + order.getTotalPrice());
        tvOrderStatus.setText("Status: " + order.getStatusMessage());

        btnOrderDetails.setOnClickListener(v -> {
            // Handle order details button click
        });

        return convertView;
    }
}

