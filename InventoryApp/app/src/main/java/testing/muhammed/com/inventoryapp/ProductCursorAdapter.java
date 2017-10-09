package testing.muhammed.com.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import testing.muhammed.com.inventoryapp.storage.InventoryContract;

/**
 * Created by muhammed on 10/9/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {
    private OnSaleButtonClickListener listener;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        TextView nameTextView = view.findViewById(R.id.productNameTextView);
        TextView priceTextView = view.findViewById(R.id.priceTextView);
        TextView quantityTextView = view.findViewById(R.id.quantityTextView);
        final Button button = view.findViewById(R.id.saleButton);
        String quantity = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryTable.QUANTITY));
        nameTextView.setText("Name : " + cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryTable.NAME)));
        priceTextView.setText("Price : " + cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryTable.PRICE)));
        quantityTextView.setText("Quantity : " + quantity);
        int obj = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryTable.ID));
        int quantityInt = Integer.parseInt(quantity);

        int[] values = {obj, quantityInt};
        button.setTag(values);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSaleClicked(((int[]) button.getTag())[0], ((int[]) button.getTag())[1]);
                }

            }
        });


    }


    public void setOnSaleButtonClickListener(OnSaleButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnSaleButtonClickListener {
        void onSaleClicked(int id, int currentQuantity);
    }
}
