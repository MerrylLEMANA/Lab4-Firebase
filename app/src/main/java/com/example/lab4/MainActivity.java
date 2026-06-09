package com.example.lab4;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrice;
    private Button buttonAdd;
    private ListView listViewProducts;
    private DatabaseReference databaseProducts;
    private List<Product> products;
    private ProductList productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewProducts = findViewById(R.id.listViewProducts);

        products = new ArrayList<>();
        productsAdapter = new ProductList(this, products);
        listViewProducts.setAdapter(productsAdapter);

        databaseProducts = FirebaseDatabase.getInstance().getReference("products");

        databaseProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product product = postSnapshot.getValue(Product.class);
                    if (product != null) {
                        products.add(product);
                    }
                }
                productsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        listViewProducts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = products.get(position);
                showUpdateDeleteDialog(product);
                return true;
            }
        });
    }

    private void addProduct() {
        String name = editTextName.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Please enter a price", Toast.LENGTH_LONG).show();
            return;
        }
        double price = Double.parseDouble(priceStr);
        String id = databaseProducts.push().getKey();
        Product product = new Product(id, name, price);
        databaseProducts.child(id).setValue(product);
        editTextName.setText("");
        editTextPrice.setText("");
        Toast.makeText(this, "Product added", Toast.LENGTH_LONG).show();
    }

    private void updateProduct(String id, String name, double price) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(id);
        Product product = new Product(id, name, price);
        dR.setValue(product);
        Toast.makeText(getApplicationContext(), "Product Updated", Toast.LENGTH_LONG).show();
    }

    private boolean deleteProduct(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(id);
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Product Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    private void showUpdateDeleteDialog(final Product product) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Update / Delete Product");
        final EditText inputName = new EditText(this);
        final EditText inputPrice = new EditText(this);
        inputName.setText(product.getName());
        inputPrice.setText(String.valueOf(product.getPrice()));
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(inputPrice);
        builder.setView(layout);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = inputName.getText().toString().trim();
            String newPriceStr = inputPrice.getText().toString().trim();
            if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newPriceStr)) {
                updateProduct(product.getId(), newName, Double.parseDouble(newPriceStr));
            }
        });
        builder.setNeutralButton("Delete", (dialog, which) -> deleteProduct(product.getId()));
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}