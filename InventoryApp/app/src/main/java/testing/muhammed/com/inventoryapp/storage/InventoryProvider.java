package testing.muhammed.com.inventoryapp.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by muhammed on 10/9/2017.
 */

public class InventoryProvider extends ContentProvider {


    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(InventoryContract.INVENTORY_AUTHORITY, InventoryContract.InventoryTable.PRODUCT_TABLE, InventoryContract.PRODUCT);
        uriMatcher.addURI(InventoryContract.INVENTORY_AUTHORITY, InventoryContract.InventoryTable.PRODUCT_TABLE + "/#", InventoryContract.PRODUCT_ID);
    }

    private InventoryDatabase database;

    @Override
    public boolean onCreate() {

        database = new InventoryDatabase(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        int match = uriMatcher.match(uri);

        SQLiteDatabase readableDatabase = database.getReadableDatabase();
        Cursor cursor = null;
        switch (match) {
            case InventoryContract.PRODUCT:
                cursor = readableDatabase.query(InventoryContract.InventoryTable.PRODUCT_TABLE, projection, selection, selectionArgs, sortOrder, null, null);
                break;
            case InventoryContract.PRODUCT_ID:
                selection = InventoryContract.InventoryTable.ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = readableDatabase.query(InventoryContract.InventoryTable.PRODUCT_TABLE, projection, selection, selectionArgs, sortOrder, null, null);
                break;
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase insertDatabase = database.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match) {
            case InventoryContract.PRODUCT:
                long insert = insertDatabase.insert(InventoryContract.InventoryTable.PRODUCT_TABLE, null, contentValues);
                if (insert > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(uri, insert);
                }

                break;
            default:
                throw new IllegalArgumentException("URI not found" + uri);
        }


        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {


        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String where, @Nullable String[] whereArgs) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase updateDatabase = database.getWritableDatabase();
        switch (match) {
            case InventoryContract.PRODUCT_ID:
                getContext().getContentResolver().notifyChange(uri, null);
                return updateDatabase.update(InventoryContract.InventoryTable.PRODUCT_TABLE, contentValues, where, whereArgs);
        }

        return -1;
    }
}
