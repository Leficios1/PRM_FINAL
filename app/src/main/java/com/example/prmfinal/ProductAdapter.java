package com.example.prmfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.prmfinal.Product;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.product_item_1, parent, false);
        }

        Product currentProduct = products.get(position);

        ImageView imageView = listItem.findViewById(R.id.product_image);
        TextView nameText = listItem.findViewById(R.id.product_name);
        TextView descriptionText = listItem.findViewById(R.id.product_description);
        TextView priceText = listItem.findViewById(R.id.product_price);
        TextView quantityText = listItem.findViewById(R.id.product_quantity);

        // Load image using Glide
        Glide.with(context)
                .load(currentProduct.getImageUrl())
                .placeholder(R.drawable.image1)
                .error(R.drawable.images)
                .into(imageView);

        nameText.setText(currentProduct.getName());
        descriptionText.setText(currentProduct.getDescription());

        // Format price
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(currentProduct.getPrice()) + " ₫";
        priceText.setText(formattedPrice);

        // Format quantity
        String quantityStr = "Còn " + currentProduct.getQuantity() + " sản phẩm";
        quantityText.setText(quantityStr);

        return listItem;
    }
}