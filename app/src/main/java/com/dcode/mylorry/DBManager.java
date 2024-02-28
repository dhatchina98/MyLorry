package com.dcode.mylorry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DataBaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    DBManager(Context c) {
        context = c;
    }

    //database open
    void open() throws SQLException {
        dbHelper = new DataBaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase read() {
        database = dbHelper.getReadableDatabase();
        return database;
    }

    //database close
    public void close() {
        dbHelper.close();
    }

    //insert in database
    void insert(String date, String from, String product, String customer, String site, String quantity, String unit, String amount, String amount_mode, String vehicle_no) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.DATE, date);
        contentValue.put(DataBaseHelper.FROM, from);
        contentValue.put(DataBaseHelper.PRODUCT, product);
        contentValue.put(DataBaseHelper.CUSTOMER, customer);
        contentValue.put(DataBaseHelper.SITE, site);
        contentValue.put(DataBaseHelper.QUANTITY, quantity);
        contentValue.put(DataBaseHelper.UNIT, unit);
        contentValue.put(DataBaseHelper.AMOUNT, amount);
        contentValue.put(DataBaseHelper.AMOUNT_MODE, amount_mode);
        contentValue.put(DataBaseHelper.VEHICLE_NO, vehicle_no);
        database.insert(DataBaseHelper.TABLE_NAME, null, contentValue);
    }

    void insert_customer(String customer_name, String mobile_no) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.CUSTOMER_NAME, customer_name);
        contentValue.put(DataBaseHelper.MOBILE_NO, mobile_no);
        database.insert(DataBaseHelper.CUSTOMER_TABLE_NAME, null, contentValue);
    }

    void insert_vendor(String vendor_name, String vendor_mobile_no) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.VENDOR_NAME, vendor_name);
        contentValue.put(DataBaseHelper.VENDOR_MOBILE_NO, vendor_mobile_no);
        database.insert(DataBaseHelper.VENDOR_TABLE_NAME, null, contentValue);
    }

    void insert_site(String site_name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.SITE_NAME, site_name);
        database.insert(DataBaseHelper.SITE_TABLE_NAME, null, contentValue);
    }

    void insert_stock(String stock_name, String stock_count) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.STOCK_NAME, stock_name);
        contentValue.put(DataBaseHelper.STOCK_COUNT, stock_count);
        database.insert(DataBaseHelper.STOCK_TABLE_NAME, null, contentValue);
    }

    void insert_vehicle(String vehicle_name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.VEHICLE_NAME, vehicle_name);
        database.insert(DataBaseHelper.VEHICLE_TABLE_NAME, null, contentValue);
    }

    void insert_payment(String payment_date, String payment_customer, String payment_amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.PAYMENT_DATE, payment_date);
        contentValue.put(DataBaseHelper.PAYMENT_CUSTOMER, payment_customer);
        contentValue.put(DataBaseHelper.PAYMENT_AMOUNT, payment_amount);
        database.insert(DataBaseHelper.PAYMENT_TABLE_NAME, null, contentValue);
    }


    //update in database
    void update(long _id, String date, String from, String product, String customer, String site, String quantity, String unit, String amount, String amount_mode, String vehicle_no) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.DATE, date);
        contentValue.put(DataBaseHelper.FROM, from);
        contentValue.put(DataBaseHelper.PRODUCT, product);
        contentValue.put(DataBaseHelper.QUANTITY, quantity);
        contentValue.put(DataBaseHelper.UNIT, unit);
        contentValue.put(DataBaseHelper.CUSTOMER, customer);
        contentValue.put(DataBaseHelper.SITE, site);
        contentValue.put(DataBaseHelper.AMOUNT, amount);
        contentValue.put(DataBaseHelper.AMOUNT_MODE, amount_mode);
        contentValue.put(DataBaseHelper.VEHICLE_NO, vehicle_no);
        int i = database.update(DataBaseHelper.TABLE_NAME, contentValue, DataBaseHelper._ID + "=" + _id, null);
    }

    void update_customer(long customer_id, String customer_name, String mobile_no) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.CUSTOMER_NAME, customer_name);
        contentValue.put(DataBaseHelper.MOBILE_NO, mobile_no);
        int i = database.update(DataBaseHelper.CUSTOMER_TABLE_NAME, contentValue, DataBaseHelper._ID + "=" + customer_id, null);
    }

    void update_vendor(long vendor_id, String vendor_name, String vendor_mobile_no) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.VENDOR_NAME, vendor_name);
        contentValue.put(DataBaseHelper.VENDOR_MOBILE_NO, vendor_mobile_no);
        int i = database.update(DataBaseHelper.VENDOR_TABLE_NAME, contentValue, DataBaseHelper._ID + "=" + vendor_id, null);
    }

    void update_site(long site_id, String site_name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.SITE_NAME, site_name);
        int i = database.update(DataBaseHelper.SITE_TABLE_NAME, contentValue, DataBaseHelper._ID + "=" + site_id, null);
    }

    void update_stock(long stock_id, String stock_name, String stock_count) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.STOCK_NAME, stock_name);
        contentValue.put(DataBaseHelper.STOCK_COUNT, stock_count);
        int i = database.update(DataBaseHelper.STOCK_TABLE_NAME, contentValue, DataBaseHelper._ID + "=" + stock_id, null);
    }

    void update_vehicle(long vehicle_id, String vehicle_name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.VEHICLE_NAME, vehicle_name);
        int i = database.update(DataBaseHelper.VEHICLE_TABLE_NAME, contentValue, DataBaseHelper._ID + "=" + vehicle_id, null);
    }

    void update_payment(long payment_id, String payment_date, String payment_customer, String payment_amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DataBaseHelper.PAYMENT_DATE, payment_date);
        contentValue.put(DataBaseHelper.PAYMENT_CUSTOMER, payment_customer);
        contentValue.put(DataBaseHelper.PAYMENT_AMOUNT, payment_amount);
        int i = database.update(DataBaseHelper.PAYMENT_TABLE_NAME, contentValue, DataBaseHelper._ID + "=" + payment_id, null);
    }

    //delete in database
    void delete(long _id) {
        database.delete(DataBaseHelper.TABLE_NAME, DataBaseHelper._ID + "=" + _id, null);
    }

    void delete_customer(long customer_id) {
        database.delete(DataBaseHelper.CUSTOMER_TABLE_NAME, DataBaseHelper._ID + "=" + customer_id, null);
    }

    void delete_vendor(long vendor_id) {
        database.delete(DataBaseHelper.VENDOR_TABLE_NAME, DataBaseHelper._ID + "=" + vendor_id, null);
    }

    void delete_site(long site_id) {
        database.delete(DataBaseHelper.SITE_TABLE_NAME, DataBaseHelper._ID + "=" + site_id, null);
    }

    void delete_stock(long stock_id) {
        database.delete(DataBaseHelper.STOCK_TABLE_NAME, DataBaseHelper._ID + "=" + stock_id, null);
    }

    void delete_vehicle(long vehicle_id) {
        database.delete(DataBaseHelper.VEHICLE_TABLE_NAME, DataBaseHelper._ID + "=" + vehicle_id, null);
    }

    void delete_payment(long payment_id) {
        database.delete(DataBaseHelper.PAYMENT_TABLE_NAME, DataBaseHelper._ID + "=" + payment_id, null);
    }

    Cursor fetch() {
        String[] columns = new String[]{DataBaseHelper._ID, DataBaseHelper.DATE, DataBaseHelper.FROM, DataBaseHelper.PRODUCT, DataBaseHelper.CUSTOMER, DataBaseHelper.SITE, DataBaseHelper.QUANTITY, DataBaseHelper.UNIT, DataBaseHelper.AMOUNT, DataBaseHelper.AMOUNT_MODE, DataBaseHelper.VEHICLE_NO};
        Cursor cursor = database.query(DataBaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    Cursor customer_fetch() {
        String[] columns1 = new String[]{DataBaseHelper._ID, DataBaseHelper.CUSTOMER_NAME, DataBaseHelper.MOBILE_NO};
        Cursor cursor1 = database.query(DataBaseHelper.CUSTOMER_TABLE_NAME, columns1, null, null, null, null, null);
        if (cursor1 != null) {
            cursor1.moveToFirst();
        }
        return cursor1;
    }

    Cursor vendor_fetch() {
        String[] columns2 = new String[]{DataBaseHelper._ID, DataBaseHelper.VENDOR_NAME, DataBaseHelper.VENDOR_MOBILE_NO};
        Cursor cursor2 = database.query(DataBaseHelper.VENDOR_TABLE_NAME, columns2, null, null, null, null, null);
        if (cursor2 != null) {
            cursor2.moveToFirst();
        }
        return cursor2;
    }

    Cursor site_fetch() {
        String[] columns = new String[]{DataBaseHelper._ID, DataBaseHelper.SITE_NAME};
        Cursor cursor = database.query(DataBaseHelper.SITE_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    Cursor stock_fetch() {
        String[] columns = new String[]{DataBaseHelper._ID, DataBaseHelper.STOCK_NAME, DataBaseHelper.STOCK_COUNT};
        Cursor cursor = database.query(DataBaseHelper.STOCK_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    Cursor vehicle_fetch() {
        String[] columns = new String[]{DataBaseHelper._ID, DataBaseHelper.VEHICLE_NAME};
        Cursor cursor = database.query(DataBaseHelper.VEHICLE_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    Cursor Bill_fetch(String selection, String[] selectionArgs) {
        String[] columns = new String[]{DataBaseHelper._ID, DataBaseHelper.DATE, DataBaseHelper.FROM, DataBaseHelper.PRODUCT, DataBaseHelper.CUSTOMER, DataBaseHelper.SITE, DataBaseHelper.QUANTITY, DataBaseHelper.UNIT, DataBaseHelper.AMOUNT, DataBaseHelper.AMOUNT_MODE, DataBaseHelper.VEHICLE_NO};
        Cursor cursor = database.query(DataBaseHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    Cursor Payment_fetch() {
        String[] columns = new String[]{DataBaseHelper._ID, DataBaseHelper.PAYMENT_DATE, DataBaseHelper.PAYMENT_CUSTOMER, DataBaseHelper.PAYMENT_AMOUNT};
        Cursor cursor = database.query(DataBaseHelper.PAYMENT_TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    Cursor Payment_fetch(String selection, String[] selectionArgs) {
        String[] columns = new String[]{DataBaseHelper._ID, DataBaseHelper.PAYMENT_DATE, DataBaseHelper.PAYMENT_CUSTOMER, DataBaseHelper.PAYMENT_AMOUNT};
        Cursor cursor = database.query(DataBaseHelper.PAYMENT_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    int Vehicle_Count() {
        return (int) DatabaseUtils.queryNumEntries(database, DataBaseHelper.VEHICLE_TABLE_NAME);
    }

    int Product_Count() {
        return (int) DatabaseUtils.queryNumEntries(database, DataBaseHelper.STOCK_TABLE_NAME);
    }

    int Site_Count() {
        return (int) DatabaseUtils.queryNumEntries(database, DataBaseHelper.SITE_TABLE_NAME);
    }

    int Customer_Count() {
        return (int) DatabaseUtils.queryNumEntries(database, DataBaseHelper.CUSTOMER_TABLE_NAME);
    }

    int Order_Count() {
        return (int) DatabaseUtils.queryNumEntries(database, DataBaseHelper.TABLE_NAME);
    }

    long Cash_Total() {
        long totalCash = 0;
        long payment = 0;
        Cursor cursor = database.rawQuery("SELECT SUM(amount) as total_cash FROM ORDERS WHERE amount_mode = 'Cash'", null);
        if (cursor.moveToFirst()) {
            totalCash = cursor.getLong(cursor.getColumnIndexOrThrow("total_cash"));
        }
        cursor.close();
        Cursor cursor1 = database.rawQuery("SELECT SUM(payment_amount) as payment FROM PAYMENT", null);
        if (cursor1.moveToFirst()) {
            payment = cursor1.getLong(cursor1.getColumnIndexOrThrow("payment"));
        }
        cursor1.close();
        return totalCash + payment;
    }

    long Credit_total() {
        long totalCash = 0;
        long payment = 0;
        Cursor cursor = database.rawQuery("SELECT SUM(amount) as total_cash FROM ORDERS WHERE amount_mode = 'Credit'", null);
        if (cursor.moveToFirst()) {
            totalCash = cursor.getLong(cursor.getColumnIndexOrThrow("total_cash"));
        }
        cursor.close();
        Cursor cursor1 = database.rawQuery("SELECT SUM(payment_amount) as payment FROM PAYMENT", null);
        if (cursor1.moveToFirst()) {
            payment = cursor1.getLong(cursor1.getColumnIndexOrThrow("payment"));
        }
        cursor1.close();
        if (totalCash < payment) {
            return 0;
        }
        return totalCash - payment;
    }

}
