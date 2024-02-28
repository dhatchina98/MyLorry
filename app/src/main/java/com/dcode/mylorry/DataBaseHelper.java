package com.dcode.mylorry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    //Table Name
    static final String TABLE_NAME = "ORDERS";
    static final String CUSTOMER_TABLE_NAME = "CUSTOMERS";
    static final String VENDOR_TABLE_NAME = "VENDORS";
    static final String SITE_TABLE_NAME = "SITES";
    static final String STOCK_TABLE_NAME = "STOCK";
    static final String VEHICLE_TABLE_NAME = "VEHICLE";
    static final String PAYMENT_TABLE_NAME = "PAYMENT";

    //Order Table Columns
    static final String _ID = "_id";
    static final String DATE = "date";
    static final String FROM = "vendor";
    static final String PRODUCT = "product";
    static final String CUSTOMER = "customer";
    static final String SITE = "site";
    static final String VEHICLE_NO = "vehicle_no";
    static final String QUANTITY = "quantity";
    static final String UNIT = "unit";
    static final String AMOUNT = "amount";
    static final String AMOUNT_MODE = "amount_mode";

    //Customer Table Columns
    static final String CUSTOMER_NAME = "customer_name";
    static final String MOBILE_NO = "mobile_no";
    //Vendors Table Columns
    static final String VENDOR_NAME = "vendor_name";
    static final String VENDOR_MOBILE_NO = "vendor_mobile_no";
    //Site Table Columns
    static final String SITE_NAME = "site_name";
    //Stock Table Columns
    static final String STOCK_NAME = "stock_name";
    static final String STOCK_COUNT = "stock_count";
    //Vehicle Table Columns
    static final String VEHICLE_NAME = "vehicle_name";
    //Payment Table Columns
    static final String PAYMENT_DATE = "payment_date";
    static final String PAYMENT_CUSTOMER = "payment_customer";
    static final String PAYMENT_AMOUNT = "payment_amount";

    //DataBase Info
    static final String DB_NAME = "my_lorry.DB";

    //DataBase Version
    static final int DB_VERSION = 13;

    //Create Table Query
    private static final String CREATE_ORDER_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT , " + DATE + " TEXT NOT NULL ," + FROM + " TEXT NOT NULL ," + PRODUCT + " TEXT NOT NULL ," + QUANTITY + " TEXT NOT NULL ," + UNIT + " TEXT NOT NULL ," + CUSTOMER + " TEXT NOT NULL ," + SITE + " TEXT NOT NULL,"+ VEHICLE_NO + " TEXT NOT NULL," + AMOUNT + " TEXT NOT NULL," + AMOUNT_MODE + " TEXT NOT NULL);";
    private static final String CREATE_CUSTOMER_TABLE = "create table " + CUSTOMER_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT , " + CUSTOMER_NAME + " TEXT NOT NULL , " + MOBILE_NO + " TEXT NOT NULL);";

    private static final String CREATE_VENDOR_TABLE = "create table " + VENDOR_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT , " + VENDOR_NAME + " TEXT NOT NULL , " + VENDOR_MOBILE_NO + " TEXT NOT NULL);";

    private static final String CREATE_SITE_TABLE = "create table " + SITE_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT , " + SITE_NAME + " TEXT NOT NULL);";
    private static final String CREATE_STOCK_TABLE = "create table " + STOCK_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT , " + STOCK_NAME + " TEXT NOT NULL, " + STOCK_COUNT + " TEXT NOT NULL);";
    private static final String CREATE_VEHICLE_TABLE = "create table " + VEHICLE_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT , " + VEHICLE_NAME + " TEXT NOT NULL);";
    private static final String CREATE_PAYMENT_TABLE = "create table " + PAYMENT_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT , " + PAYMENT_DATE + " TEXT NOT NULL, " + PAYMENT_CUSTOMER + " TEXT NOT NULL, " + PAYMENT_AMOUNT +  " TEXT NOT NULL);";
    DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db1) {
        db1.execSQL(CREATE_ORDER_TABLE);
        db1.execSQL(CREATE_CUSTOMER_TABLE);
        db1.execSQL(CREATE_VENDOR_TABLE);
        db1.execSQL(CREATE_SITE_TABLE);
        db1.execSQL(CREATE_STOCK_TABLE);
        db1.execSQL(CREATE_VEHICLE_TABLE);
        db1.execSQL(CREATE_PAYMENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VENDOR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SITE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STOCK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VEHICLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PAYMENT_TABLE_NAME);
        onCreate(db);
    }
}
