package testing.muhammed.com.inventoryapp.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import testing.muhammed.com.inventoryapp.ProductCursorAdapter;
import testing.muhammed.com.inventoryapp.R;
import testing.muhammed.com.inventoryapp.storage.InventoryContract;

public class OverallActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, ProductCursorAdapter.OnSaleButtonClickListener, AdapterView.OnItemClickListener {

    private ListViewCompat mOverallListViewCompat;

    private Button mAddButton;
    private ProductCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall);

        initUI();
    }

    private void initUI() {

        mAddButton = (Button) findViewById(R.id.addButton);
        mOverallListViewCompat = (ListViewCompat) findViewById(R.id.overallListView);

        mOverallListViewCompat.setDivider(new ColorDrawable(getResources().getColor(R.color.black)));
        mOverallListViewCompat.setDividerHeight(2);

        cursorAdapter = new ProductCursorAdapter(this, null);
        cursorAdapter.setOnSaleButtonClickListener(this);

        mOverallListViewCompat.setAdapter(cursorAdapter);

        mOverallListViewCompat.setOnItemClickListener(this);

        mAddButton.setOnClickListener(this);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addButton:
                startActivity(new Intent(this, AddProductActivity.class));
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.InventoryTable.ID,
                InventoryContract.InventoryTable.NAME,
                InventoryContract.InventoryTable.PRICE,
                InventoryContract.InventoryTable.QUANTITY
        };

        String selection = InventoryContract.InventoryTable.QUANTITY + ">?";
        String selectionArg[] = {"0"};

        return new CursorLoader(this, InventoryContract.INVENTORY_PRODUCT, projection, selection, selectionArg, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onSaleClicked(int id, int currentQuantity) {
        Toast.makeText(this, "" + currentQuantity, Toast.LENGTH_SHORT).show();
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryTable.QUANTITY, currentQuantity - 1);
        int update = getContentResolver().update(Uri.withAppendedPath(InventoryContract.INVENTORY_PRODUCT_ID, "" + id),
                values, InventoryContract.InventoryTable.ID + "=?", new String[]{String.valueOf(id)});

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
        int obj = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryTable.ID));

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(InventoryContract.InventoryTable.ID, obj);
        startActivity(intent);

    }
}
