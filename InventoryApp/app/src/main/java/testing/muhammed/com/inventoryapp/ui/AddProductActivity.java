package testing.muhammed.com.inventoryapp.ui;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import testing.muhammed.com.inventoryapp.R;
import testing.muhammed.com.inventoryapp.storage.InventoryContract;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSIONS_REQUEST_CAMERA = 10101;
    private static final int CAPTURE_IMAGE = 12;
    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;

    private Button mIncreaseButton;
    private Button mDecreaseButton;
    private Button mAddImageButton;
    private Button mAddProductButton;
    private ImageView mProductImageView;

    private int quantity = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initUI();
    }

    private void initUI() {

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mQuantityEditText = (EditText) findViewById(R.id.quantityEditText);
        mPriceEditText = (EditText) findViewById(R.id.priceEditText);

        mIncreaseButton = (Button) findViewById(R.id.increaseButton);
        mDecreaseButton = (Button) findViewById(R.id.decreaseButton);
        mAddImageButton = (Button) findViewById(R.id.addImageButton);
        mAddProductButton = (Button) findViewById(R.id.addButton);
        mProductImageView = (ImageView) findViewById(R.id.productImageView);


        mIncreaseButton.setOnClickListener(this);
        mDecreaseButton.setOnClickListener(this);
        mAddImageButton.setOnClickListener(this);
        mAddProductButton.setOnClickListener(this);

        mQuantityEditText.setText(quantity + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.increaseButton:
                quantity++;
                break;
            case R.id.decreaseButton:
                if (quantity == 0) {
                    Toast.makeText(this, "Quantity must be 0 or greater", Toast.LENGTH_SHORT).show();
                } else {
                    quantity--;
                }
                break;
            case R.id.addImageButton:

                startCameraIntent();
                break;
            case R.id.addButton:

                String name = mNameEditText.getText().toString();
                String quantity = mQuantityEditText.getText().toString();
                String price = mPriceEditText.getText().toString();

                if (name.length() <= 0) {
                    Toast.makeText(this, "Enter product name", Toast.LENGTH_SHORT).show();

                } else if (quantity.length() <= 0) {
                    Toast.makeText(this, "Enter quantity", Toast.LENGTH_SHORT).show();

                } else if (price.length() <= 0) {
                    Toast.makeText(this, "Please enter the price", Toast.LENGTH_SHORT).show();

                } else if (Integer.parseInt(quantity) < 0) {
                    this.quantity = 0;
                    Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();

                } else {
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.InventoryTable.NAME, name);
                    values.put(InventoryContract.InventoryTable.QUANTITY, AddProductActivity.this.quantity);
                    values.put(InventoryContract.InventoryTable.PRICE, Integer.parseInt(price));
                    //TODO dummy image processing now

                    values.put(InventoryContract.InventoryTable.IMAGE, "test.jpg");
                    Uri uri = getContentResolver().insert(InventoryContract.INVENTORY_PRODUCT, values);
                    if (uri != null) {
                        long id = ContentUris.parseId(uri);
                        Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
                    }
                }


                break;
        }
        mQuantityEditText.setText(quantity + "");
    }

    private void startCameraIntent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(this, "Need permission to access camera", Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSIONS_REQUEST_CAMERA);
                }
            } else {

                startAction();
            }
        } else {
            startAction();
        }

    }

    private void startAction() {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    startCameraIntent();
                } else {
                    Toast.makeText(this, "Need permission to access camera", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE:
                if (resultCode == RESULT_OK) {

                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mProductImageView.setImageBitmap(imageBitmap);
                }
                break;
        }
    }
}
