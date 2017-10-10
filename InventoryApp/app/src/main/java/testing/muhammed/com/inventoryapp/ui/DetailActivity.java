package testing.muhammed.com.inventoryapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import testing.muhammed.com.inventoryapp.R;
import testing.muhammed.com.inventoryapp.storage.InventoryContract;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;
    private Button mIncreaseButton;
    private Button mDecreaseButton;
    private Button mOrderButton;
    private Button mDeleteButton;
    private int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        initUI();

    }

    private void initUI() {
        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mQuantityEditText = (EditText) findViewById(R.id.quantityEditText);
        mPriceEditText = (EditText) findViewById(R.id.priceEditText);
        mIncreaseButton = (Button) findViewById(R.id.increaseButton);
        mDecreaseButton = (Button) findViewById(R.id.decreaseButton);
        mOrderButton = (Button) findViewById(R.id.orderButton);
        mDeleteButton = (Button) findViewById(R.id.deleteButton);

        mIncreaseButton.setOnClickListener(this);
        mDecreaseButton.setOnClickListener(this);
        mOrderButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);

        int id = getIntent().getIntExtra(InventoryContract.InventoryTable.ID, -1);

        String[] projection = {
                InventoryContract.InventoryTable.NAME,
                InventoryContract.InventoryTable.QUANTITY,
                InventoryContract.InventoryTable.PRICE,
                InventoryContract.InventoryTable.ID
        };

        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(InventoryContract.INVENTORY_PRODUCT_ID, String.valueOf(id)), projection, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            mNameEditText.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryTable.NAME)));
            mQuantityEditText.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryTable.QUANTITY)));
            mPriceEditText.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryTable.PRICE)));
            quantity = Integer.parseInt(mQuantityEditText.getText().toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.increaseButton:
                quantity++;
                break;
            case R.id.decreaseButton:
                if (quantity == 0) {
                    Toast.makeText(this, R.string.quantitity_error, Toast.LENGTH_SHORT).show();
                } else {
                    quantity--;
                }
                break;
            case R.id.orderButton:

                composeEmail(new String[]{""}, getString(R.string.email_subject));


                break;
            case R.id.deleteButton:

                showDeleteDialog();
                break;
        }
        mQuantityEditText.setText(String.valueOf(quantity));
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this record?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int id = getIntent().getIntExtra(InventoryContract.InventoryTable.ID, -1);
                getContentResolver().delete(Uri.withAppendedPath(InventoryContract.INVENTORY_PRODUCT_ID, String.valueOf(id)), null, null);
                finish();
            }
        });

        builder.setNegativeButton("NO", null);
        builder.create().show();
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);


        StringBuilder builder = new StringBuilder();
        builder.append("Name : ").append(mNameEditText.getText().toString())
                .append("\n")
                .append("Quantity : ")
                .append(mQuantityEditText.getText().toString())
                .append("\n")
                .append("Price :")
                .append(mPriceEditText.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
