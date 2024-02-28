package com.dcode.mylorry;

import static com.dcode.mylorry.ListView_ImageBtn_AddOrder.getArray;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class modify_order extends AppCompatActivity implements View.OnClickListener {


    private EditText dateEditText;
    private static AutoCompleteTextView fromTextView;
    private static AutoCompleteTextView productTextView;
    private static AutoCompleteTextView customerTextView;
    private static AutoCompleteTextView siteTextView;
    private static AutoCompleteTextView vehicleTextView;
    private EditText quantityEditText;
    private EditText amountEditText;
    private Spinner unit_spinner;
    private Spinner amount_mode_spinner;
    private DBManager dbManager;

    Calendar calender;

    String[] arrCustomer;
    String[] arrFrom;
    String[] arrProduct;
    String[] arrSite;
    String[] arrVehicle;
    String[] arrUnit;


    ArrayAdapter<String> FromAdapter;
    ArrayAdapter<String> ProductAdapter;
    ArrayAdapter<String> CustomerAdapter;
    ArrayAdapter<String> SiteAdapter;
    ArrayAdapter<String> VehicleAdapter;

    public String selected_Value;
    public static String selected_List;
    private long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_order);
        calender = Calendar.getInstance();
        dbManager = new DBManager(this);
        dbManager.open();
        SQLiteDatabase db = dbManager.read();
        selected_Value = "";
        selected_List = "";
        dateEditText = findViewById(R.id.date);
        fromTextView = findViewById(R.id.from);
        productTextView = findViewById(R.id.product);
        customerTextView = findViewById(R.id.customer);
        siteTextView = findViewById(R.id.site);
        quantityEditText = findViewById(R.id.quantity);
        unit_spinner = findViewById(R.id.unit_spinner);
        amountEditText = findViewById(R.id.amount);
        amount_mode_spinner = findViewById(R.id.amount_spinner);
        vehicleTextView = findViewById(R.id.vehicle_no);
        Button updateBtn = findViewById(R.id.btn_update);

        ImageButton dateBtn = findViewById(R.id.dateBtn);
        ImageButton fromBtn = findViewById(R.id.fromBtn);
        ImageButton productBtn = findViewById(R.id.productBtn);
        ImageButton customerBtn = findViewById(R.id.customerBtn);
        ImageButton siteBtn = findViewById(R.id.siteBtn);
        ImageButton vehicleBtn = findViewById(R.id.vehicleBtn);
        updateBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        fromBtn.setOnClickListener(this);
        productBtn.setOnClickListener(this);
        customerBtn.setOnClickListener(this);
        siteBtn.setOnClickListener(this);
        vehicleBtn.setOnClickListener(this);
        //spinner for unit
        Cursor cursor5 = db.rawQuery("SELECT * FROM " + "STOCK", null);
        Set<String> unitSet = new HashSet<>();
        if (cursor5.moveToFirst()) {
            do {
                String data = cursor5.getString(cursor5.getColumnIndexOrThrow("stock_count"));
                unitSet.add(data);
            } while (cursor5.moveToNext());
        }
        cursor5.close();
        arrUnit = new String[unitSet.size()];
        int index=0;
        for (String value:unitSet) {
            arrUnit[index] = value;
            index++;
        }
        unit_spinner = findViewById(R.id.unit_spinner);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrUnit);
        // Optionally, set the dropdown layout resource if using a spinner
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter to your spinner
        unit_spinner.setAdapter(unitAdapter);
        //spinner for cash
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.cash_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        amount_mode_spinner.setAdapter(adapter1);
        //Adapter For AutoFilling
        //From
        Cursor cursor = db.rawQuery("SELECT * FROM " + "VENDORS", null);
        ArrayList<String> arrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor.getColumnIndexOrThrow("vendor_name"));
                arrayList.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        arrFrom = new String[arrayList.size()];
        for (int i = 0; i < arrFrom.length; i++) {
            arrFrom[i] = arrayList.get(i);
        }
        FromAdapter = new ArrayAdapter<>(this, R.layout.list_item, arrFrom);
        fromTextView.setAdapter(FromAdapter);
        fromTextView.setOnItemClickListener((parent, view, i, l) -> {
            String item = parent.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), item + " selected", Toast.LENGTH_SHORT).show();
        });
        //Product
        Cursor cursor1 = db.rawQuery("SELECT * FROM " + "STOCK", null);
        ArrayList<String> arrayList1 = new ArrayList<>();
        if (cursor1.moveToFirst()) {
            do {
                String data = cursor1.getString(cursor1.getColumnIndexOrThrow("stock_name"));
                arrayList1.add(data);
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        arrProduct = new String[arrayList1.size()];
        for (int i = 0; i < arrProduct.length; i++) {
            arrProduct[i] = arrayList1.get(i);
        }
        ProductAdapter = new ArrayAdapter<>(this, R.layout.list_item, arrProduct);
        productTextView.setAdapter(ProductAdapter);
        productTextView.setOnItemClickListener((parent, view, i, l) -> {
            String item = parent.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), item + " selected", Toast.LENGTH_SHORT).show();
        });
        //Customer
        Cursor cursor2 = db.rawQuery("SELECT * FROM " + "CUSTOMERS", null);
        ArrayList<String> arrayList2 = new ArrayList<>();
        if (cursor2.moveToFirst()) {
            do {
                String data = cursor2.getString(cursor2.getColumnIndexOrThrow("customer_name"));
                arrayList2.add(data);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
        arrCustomer = new String[arrayList2.size()];
        for (int i = 0; i < arrCustomer.length; i++) {
            arrCustomer[i] = arrayList2.get(i);
        }
        CustomerAdapter = new ArrayAdapter<>(this, R.layout.list_item, arrCustomer);
        customerTextView.setAdapter(CustomerAdapter);
        customerTextView.setOnItemClickListener((parent, view, i, l) -> {
            String item = parent.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), item + " selected", Toast.LENGTH_SHORT).show();
        });
        //Site
        Cursor cursor3 = db.rawQuery("SELECT * FROM " + "SITES", null);
        ArrayList<String> arrayList3 = new ArrayList<>();
        if (cursor3.moveToFirst()) {
            do {
                String data = cursor3.getString(cursor3.getColumnIndexOrThrow("site_name"));
                arrayList3.add(data);
            } while (cursor3.moveToNext());
        }
        cursor3.close();
        arrSite = new String[arrayList3.size()];
        for (int i = 0; i < arrSite.length; i++) {
            arrSite[i] = arrayList3.get(i);
        }
        SiteAdapter = new ArrayAdapter<>(this, R.layout.list_item, arrSite);
        siteTextView.setAdapter(SiteAdapter);
        siteTextView.setOnItemClickListener((parent, view, i, l) -> {
            String item = parent.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), item + " selected", Toast.LENGTH_SHORT).show();
        });
        //Vehicle
        Cursor cursor4 = db.rawQuery("SELECT * FROM " + "VEHICLE", null);
        ArrayList<String> arrayList4 = new ArrayList<>();
        if (cursor4.moveToFirst()) {
            do {
                String data = cursor4.getString(cursor4.getColumnIndexOrThrow("vehicle_name"));
                arrayList4.add(data);
            } while (cursor4.moveToNext());
        }
        cursor4.close();
        arrVehicle = new String[arrayList4.size()];
        for (int i = 0; i < arrVehicle.length; i++) {
            arrVehicle[i] = arrayList4.get(i);
        }
        VehicleAdapter = new ArrayAdapter<>(this, R.layout.list_item, arrVehicle);
        vehicleTextView.setAdapter(VehicleAdapter);
        vehicleTextView.setOnItemClickListener((parent, view, i, l) -> {
            String item = parent.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), item + " selected", Toast.LENGTH_SHORT).show();
        });
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String date = intent.getStringExtra("date");
        String from = intent.getStringExtra("from");
        String product = intent.getStringExtra("product");
        String customer = intent.getStringExtra("customer");
        String site = intent.getStringExtra("site");
        String quantity = intent.getStringExtra("quantity");
        String unit = intent.getStringExtra("unit");
        String amount = intent.getStringExtra("amount");
        String amount_mode = intent.getStringExtra("amount_mode");
        String vehicle_no = intent.getStringExtra("vehicle_no");

        _id = Long.parseLong(id);
        dateEditText.setText(date);
        fromTextView.setText(from);
        productTextView.setText(product);
        customerTextView.setText(customer);
        siteTextView.setText(site);
        quantityEditText.setText(quantity);
        vehicleTextView.setText(vehicle_no);
        int unit_Position = unitAdapter.getPosition(unit);
        unit_spinner.setSelection(unit_Position);
        amountEditText.setText(amount);
        int cash_Position = adapter1.getPosition(amount_mode);
        amount_mode_spinner.setSelection(cash_Position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_update) {
            String date = dateEditText.getText().toString();
            String from = fromTextView.getText().toString();
            String product = productTextView.getText().toString();
            String customer = customerTextView.getText().toString();
            String site = siteTextView.getText().toString();
            String quantity = quantityEditText.getText().toString();
            String unit;
            try{
                unit = unit_spinner.getSelectedItem().toString();
            }catch (NullPointerException ex){
                unit = " - ";
            }
            String amount = amountEditText.getText().toString();
            String amount_mode = amount_mode_spinner.getSelectedItem().toString();
            String vehicle_no = vehicleTextView.getText().toString();
            if(date.trim().isEmpty()){
                dateEditText.setError("Enter Date");
            }else if(from.trim().isEmpty()){
                fromTextView.setError("Enter Vendor");
            }else if(product.trim().isEmpty()){
                productTextView.setError("Enter Product");
            }else if(customer.trim().isEmpty()){
                customerTextView.setError("Enter Customer");
            }else if(site.trim().isEmpty()){
                siteTextView.setError("Enter Site");
            }else if(vehicle_no.trim().isEmpty()){
                vehicleTextView.setError("Enter Vehicle");
            }else if(quantity.trim().isEmpty()){
                quantityEditText.setError("Enter Quantity");
            }else if(amount.trim().isEmpty()){
                amountEditText.setError("Enter Amount");
            }else{
                dbManager.update(_id, date, from, product, customer, site, quantity, unit, amount, amount_mode, vehicle_no);
                this.returnHome();
            }
        }  else if (v.getId() == R.id.dateBtn) {
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH);
            int day = calender.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(modify_order.this, R.style.MyDatePickerDialogTheme,(datePicker, year_1, month_1, day_1) -> {
                String formattedDate = String.format(Locale.US, "%02d/%02d/%04d", day_1, month_1 + 1, year_1);
                dateEditText.setText(formattedDate);
            }, year, month, day).show();        } else if (v.getId() == R.id.fromBtn) {
            selected_List = "From";
            call_ListView(arrFrom, "Vendor");
        } else if (v.getId() == R.id.productBtn) {
            selected_List = "Product";
            call_ListView(arrProduct, "Product");
        } else if (v.getId() == R.id.customerBtn) {
            selected_List = "Customer";
            call_ListView(arrCustomer, "Customer");
        } else if (v.getId() == R.id.siteBtn) {
            selected_List = "Site";
            call_ListView(arrSite, "Site");
        } else if (v.getId() == R.id.vehicleBtn) {
            selected_List = "Vehicle";
            call_ListView(arrVehicle, "Vehicle");
        }
    }

    public void call_ListView(String[] arr, String str) {
        String call_from = "modify";
        Intent a = new Intent(getApplicationContext(), ListView_ImageBtn_AddOrder.class);
        a.putExtra("call_from", call_from);
        getArray(arr, str);
        startActivity(a);
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), on_click_btn_order_list.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    public void getSelectedValue(String str) {
        selected_Value = str;
        switch (selected_List) {
            case "From":
                fromTextView.setText(selected_Value);
                break;
            case "Product":
                productTextView.setText(selected_Value);
                break;
            case "Customer":
                customerTextView.setText(selected_Value);
                break;
            case "Site":
                siteTextView.setText(selected_Value);
                break;
            case "Vehicle":
                vehicleTextView.setText(selected_Value);
                break;
        }


    }
}