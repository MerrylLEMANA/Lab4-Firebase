package com.example.lab4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class ProductList extends ArrayAdapter<Product> {

    public ProductList(Context context, List<Product> products) {
        super(context, R.layout.list_item_product, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_product, parent, false);
        }
        Product product = getItem(position);
        TextView nameView = convertView.findViewById(R.id.textViewName);
        TextView priceView = convertView.findViewById(R.id.textViewPrice);
        nameView.setText(product.getName());
        priceView.setText(String.valueOf(product.getPrice()));
        return convertView;
    }
}