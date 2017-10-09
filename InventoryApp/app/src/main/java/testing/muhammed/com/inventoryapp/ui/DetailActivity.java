package testing.muhammed.com.inventoryapp.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import testing.muhammed.com.inventoryapp.R;
import testing.muhammed.com.inventoryapp.storage.InventoryContract;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        int id = getIntent().getIntExtra(InventoryContract.InventoryTable.ID, -1);

        String[] projection = {
                InventoryContract.InventoryTable.NAME,
                InventoryContract.InventoryTable.QUANTITY,
                InventoryContract.InventoryTable.PRICE,
                InventoryContract.InventoryTable.ID
        };

        Cursor cursor = getContentResolver().query(InventoryContract.INVENTORY_PRODUCT, projection, null, null, null);
    }
}
