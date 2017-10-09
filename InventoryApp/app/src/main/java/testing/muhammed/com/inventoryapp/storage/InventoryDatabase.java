package testing.muhammed.com.inventoryapp.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by muhammed on 10/9/2017.
 */

public class InventoryDatabase extends SQLiteOpenHelper {
    public InventoryDatabase(Context context) {
        super(context, InventoryContract.DB_NAME, null, InventoryContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(InventoryContract.InventoryTable.CREATE_INVENTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
