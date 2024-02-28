package com.dcode.mylorry;

import static com.dcode.mylorry.ListView_ImageBtn_AddOrder.getArray;
import static com.dcode.mylorry.ListView_ImageBtn_AddOrder.items;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class make_bill extends AppCompatActivity implements View.OnClickListener, PDFUtility.OnDocumentClose {

    private EditText start_dateEditText;
    private EditText end_dateEditText;
    private static AutoCompleteTextView fromTextView;
    private static AutoCompleteTextView productTextView;
    private static AutoCompleteTextView customerTextView;
    private static AutoCompleteTextView siteTextView;
    private static AutoCompleteTextView vehicleTextView;
    private DBManager dbManager;
    private SQLiteDatabase db;
    private int day;
    private int month;
    private int year;
    Calendar calender;
    String[] arrCustomer;
    String[] arrFrom;
    String[] arrProduct;
    String[] arrSite;
    String[] arrVehicle;
    private Spinner bill_spinner;
    ArrayAdapter<String> FromAdapter;
    ArrayAdapter<String> ProductAdapter;
    ArrayAdapter<String> CustomerAdapter;
    ArrayAdapter<String> SiteAdapter;
    ArrayAdapter<String> VehicleAdapter;
    public String selected_Value;
    public static String selected_List;

    private static final String TAG = First_Activity.class.getSimpleName();
    private AppCompatEditText rowCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_bill);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Bill");
        }
        selected_Value = "";
        selected_List = "";
        start_dateEditText = findViewById(R.id.from_date);
        end_dateEditText = findViewById(R.id.to_date);
        Drawable backgroundDrawable1 = ContextCompat.getDrawable(make_bill.this, R.drawable.underline);
        Drawable backgroundDrawable2 = ContextCompat.getDrawable(make_bill.this, R.drawable.underline);
        Drawable backgroundDrawable3 = ContextCompat.getDrawable(make_bill.this, R.drawable.underline);
        Drawable backgroundDrawable4 = ContextCompat.getDrawable(make_bill.this, R.drawable.underline);
        Drawable backgroundDrawable5 = ContextCompat.getDrawable(make_bill.this, R.drawable.underline);
        fromTextView = findViewById(R.id.from_b);
        fromTextView.setBackground(backgroundDrawable1);
        productTextView = findViewById(R.id.product_b);
        productTextView.setBackground(backgroundDrawable2);
        customerTextView = findViewById(R.id.customer_b);
        customerTextView.setBackground(backgroundDrawable3);
        siteTextView = findViewById(R.id.site_b);
        siteTextView.setBackground(backgroundDrawable4);
        vehicleTextView = findViewById(R.id.vehicle_b);
        vehicleTextView.setBackground(backgroundDrawable5);
        Button createButton = findViewById(R.id.create_button);
        ImageButton from_dateBtn = findViewById(R.id.from_dateBtn);
        ImageButton to_dateBtn = findViewById(R.id.to_dateBtn);
        ImageButton fromBtn = findViewById(R.id.fromBtn);
        ImageButton productBtn = findViewById(R.id.productBtn);
        ImageButton customerBtn = findViewById(R.id.customerBtn);
        ImageButton siteBtn = findViewById(R.id.siteBtn);
        ImageButton vehicleBtn = findViewById(R.id.vehicleBtn);
        //opening database
        dbManager = new DBManager(this);
        dbManager.open();
        db = dbManager.read();
        //OnClick Listener For Add Button
        from_dateBtn.setOnClickListener(this);
        to_dateBtn.setOnClickListener(this);
        createButton.setOnClickListener(this);
        fromBtn.setOnClickListener(this);
        productBtn.setOnClickListener(this);
        customerBtn.setOnClickListener(this);
        siteBtn.setOnClickListener(this);
        vehicleBtn.setOnClickListener(this);
        calender = Calendar.getInstance();
        //First Time Setting Current Date
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH);
        String month_temp1 = (month < 10) ? ("0" + month) : String.valueOf(month);
        String month_temp2 = ((month + 1) < 10) ? ("0" + (month + 1)) : String.valueOf(month + 1);
        day = calender.get(Calendar.DAY_OF_MONTH);
        String dayTemp = (day < 10) ? ("0" + day) : String.valueOf(day);
        start_dateEditText.setText(dayTemp + "/" + month_temp1 + "/" + year);
        end_dateEditText.setText(dayTemp + "/" + month_temp2 + "/" + year);
        //Adapter For AutoFilling
        bill_spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bill_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bill_spinner.setAdapter(adapter);
        bill_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Perform the desired action based on the selected item
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(make_bill.this, selectedItem + " Selected", Toast.LENGTH_SHORT).show();
                if (selectedItem.equals("Customer_Bill")) {
                    fromTextView.setError(null);
                    if (!fromTextView.isEnabled()) {
                        fromTextView.setEnabled(true);
                    }
                    if (!productTextView.isEnabled()) {
                        productTextView.setEnabled(true);
                    }
                    if (!customerTextView.isEnabled()) {
                        customerTextView.setEnabled(true);
                    }
                    if (!siteTextView.isEnabled()) {
                        siteTextView.setEnabled(true);
                    }
                    if (!vehicleTextView.isEnabled()) {
                        vehicleTextView.setEnabled(true);
                    }
                } else if (selectedItem.equals("Order_Bill")) {
                    fromTextView.setError(null);
                    customerTextView.setError(null);
                    fromTextView.setText("");
                    productTextView.setText("");
                    customerTextView.setText("");
                    siteTextView.setText("");
                    vehicleTextView.setText("");
                    if (fromTextView.isEnabled()) {
                        fromTextView.setEnabled(false);
                    }
                    if (productTextView.isEnabled()) {
                        productTextView.setEnabled(false);
                    }
                    if (customerTextView.isEnabled()) {
                        customerTextView.setEnabled(false);
                    }
                    if (siteTextView.isEnabled()) {
                        siteTextView.setEnabled(false);
                    }
                    if (vehicleTextView.isEnabled()) {
                        vehicleTextView.setEnabled(false);
                    }
                } else if (selectedItem.equals("Vendor_Bill")) {
                    customerTextView.setError(null);
                    productTextView.setText("");
                    customerTextView.setText("");
                    siteTextView.setText("");
                    vehicleTextView.setText("");
                    if (!fromTextView.isEnabled()) {
                        fromTextView.setEnabled(true);
                    }
                    if (productTextView.isEnabled()) {
                        productTextView.setEnabled(false);
                    }
                    if (customerTextView.isEnabled()) {
                        customerTextView.setEnabled(false);
                    }
                    if (siteTextView.isEnabled()) {
                        siteTextView.setEnabled(false);
                    }
                    if (vehicleTextView.isEnabled()) {
                        vehicleTextView.setEnabled(false);
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
    }


    //button onclick value operations
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_button) {
            final String from_date = start_dateEditText.getText().toString();
            final String to_date = end_dateEditText.getText().toString();
            final String from = fromTextView.getText().toString();
            final String product = productTextView.getText().toString();
            final String customer = customerTextView.getText().toString();
            final String site = siteTextView.getText().toString();
            final String vehicle = vehicleTextView.getText().toString();
            final String Bill_Type = bill_spinner.getSelectedItem().toString();
            if (from_date.trim().isEmpty()) {
                start_dateEditText.setError("Enter Date");
            } else if (to_date.trim().isEmpty()) {
                end_dateEditText.setError("Enter Date");
            } else {
                String path = null;
                String folderName = Bill_Type + "s"; // Specify the name of your folder
                File folder = new File(getExternalFilesDir(null), folderName);
//                File externalStorageDir = Environment.getExternalStorageDirectory();
//                File folder = new File(externalStorageDir, "Documents/MyLorryStorage/"+folderName);
                if (!folder.exists()) {
                    if (folder.mkdirs()) {
                        path = folder.getAbsolutePath();
                    }
                } else {
                    path = folder.getAbsolutePath();
                }
                List<String[]> data = null;
                List<String[]> Amount_data = null;
                String[] table_columns = null;
                String filepath = null;
                boolean isReady = false;
                boolean is_Portrait = true;
                String title = null;
                String subtitle = "From : " + from_date + "  To : " + to_date;
                List<String> dates = giveAllDay(from_date, to_date);
                if (Bill_Type.equals("Customer_Bill")) {
                    if (customer.trim().isEmpty()) {
                        customerTextView.setError("Enter Customer");
                    } else {
                        isReady = true;
                        title = customer + " Bill";
                        table_columns = new String[]{"date", "vehicle_no", "product", "quantity", "unit", "site", "amount", "amount_mode"};
                        data = getData(dates, from, product, customer, site, vehicle, table_columns);
                        String[] amnt_columns = new String[]{"payment_date", "payment_amount"};
                        Amount_data = getAmountData(dates, customer, amnt_columns);
                        filepath = path + "/" + customer + "_Bill_" + getDateString(from_date.trim()) + "_" + getDateString(to_date.trim()) + ".pdf";
                    }
                } else if (Bill_Type.equals("Vendor_Bill")) {
                    if (from.trim().isEmpty()) {
                        fromTextView.setError("Enter Vendor");
                    } else {
                        isReady = true;
                        title = from + " Bill";
                        table_columns = new String[]{"date", "vehicle_no", "product", "quantity"};
                        data = getData(dates, from, "", "", "", "", table_columns);
                        filepath = path + "/" + from + "_Bill_" + getDateString(from_date.trim()) + "_" + getDateString(to_date.trim()) + ".pdf";
                    }
                } else if (Bill_Type.equals("Order_Bill")) {
                    isReady = true;
                    is_Portrait = false;
                    title = "Order Bill";
                    table_columns = new String[]{"date", "vehicle_no", "vendor", "site", "customer", "product", "quantity", "unit", "amount", "amount_mode"};
                    data = getData(dates, "", "", "", "", "", table_columns);
                    filepath = path + "/Orders_Bill_" + getDateString(from_date.trim()) + "_" + getDateString(to_date.trim()) + ".pdf";
                }
                if (isReady) {
                    if (!data.isEmpty()) {
                        try {
                            PDFUtility.createPdf(v.getContext(), make_bill.this, data, filepath, is_Portrait, Bill_Type, title, subtitle, Amount_data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error Create Pdf");
                            Toast.makeText(v.getContext(), "Can't Create Bill", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Context context = getApplicationContext();
                        Toast.makeText(context, "No Orders", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (v.getId() == R.id.from_dateBtn) {
            year = calender.get(Calendar.YEAR);
            month = calender.get(Calendar.MONTH);
            day = calender.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(make_bill.this, R.style.MyDatePickerDialogTheme,(datePicker, year, month, day) -> {
                String formattedDate = String.format(Locale.US, "%02d/%02d/%04d", day, month + 1, year);
                start_dateEditText.setText(formattedDate);
            }, year, month, day).show();
        } else if (v.getId() == R.id.to_dateBtn) {
            year = calender.get(Calendar.YEAR);
            month = calender.get(Calendar.MONTH);
            day = calender.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(make_bill.this,  R.style.MyDatePickerDialogTheme,(datePicker, year, month, day) -> {
                String formattedDate = String.format(Locale.US, "%02d/%02d/%04d", day, month + 1, year);
                end_dateEditText.setText(formattedDate);
            }, year, month, day).show();

        } else if (v.getId() == R.id.fromBtn) {
            selected_List = "From";
            call_ListView(arrFrom, "none");
        } else if (v.getId() == R.id.productBtn) {
            selected_List = "Product";
            call_ListView(arrProduct, "none");
        } else if (v.getId() == R.id.customerBtn) {
            selected_List = "Customer";
            call_ListView(arrCustomer, "none");
        } else if (v.getId() == R.id.siteBtn) {
            selected_List = "Site";
            call_ListView(arrSite, "none");
        } else if (v.getId() == R.id.vehicleBtn) {
            selected_List = "Vehicle";
            call_ListView(arrVehicle, "none");
        }
    }

    public void call_ListView(String[] arr, String str) {
        String call_from = "bill";
        Intent a = new Intent(getApplicationContext(), ListView_ImageBtn_AddOrder.class);
        a.putExtra("call_from", call_from);
        getArray(arr, str);
        if(items.length>0){
            startActivity(a);
        }else{
            Toast.makeText(this, "List is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private String getDateString(String date) {
        String[] arr = date.split("/");
        return arr[0] + arr[1] + arr[2];
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

    @Override
    public void onPDFDocumentClose(File file) {
        Toast.makeText(this, "Bill Created Successfully", Toast.LENGTH_SHORT).show();
    }

    private String repeat(String str, int count, String delimiter) {
        StringBuilder repeated = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                repeated.append(delimiter);
            }
            repeated.append(str);
        }
        return repeated.toString();
    }

    private List<String[]> getData(List<String> dates, String from, String product, String customer, String site, String vehicle, String[] columnArr) {
        StringBuilder selectionBuilder = new StringBuilder();
        List<String> list_SelectionArgs = new ArrayList<>();
        list_SelectionArgs.addAll(dates);
        selectionBuilder.append("date IN (" + repeat("?", dates.size(), ", ") + ")");
        if (!from.trim().isEmpty()) {
            selectionBuilder.append(" AND vendor = ?");
            list_SelectionArgs.add(from);
        }
        if (!product.trim().isEmpty()) {
            selectionBuilder.append(" AND product = ?");
            list_SelectionArgs.add(product);
        }
        if (!customer.trim().isEmpty()) {
            selectionBuilder.append(" AND customer = ?");
            list_SelectionArgs.add(customer);
        }
        if (!site.trim().isEmpty()) {
            selectionBuilder.append(" AND site = ?");
            list_SelectionArgs.add(site);
        }
        if (!vehicle.trim().isEmpty()) {
            selectionBuilder.append(" AND vehicle_no = ?");
            list_SelectionArgs.add(vehicle);
        }

        String[] selectionArgs = list_SelectionArgs.toArray(new String[list_SelectionArgs.size()]);
        List<String[]> data = new ArrayList<>();
        Cursor cursor = dbManager.Bill_fetch(selectionBuilder.toString(), selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] Arr = new String[columnArr.length];
            for (int i = 0; i < Arr.length; i++) {
                Arr[i] = cursor.getString(cursor.getColumnIndexOrThrow(columnArr[i]));
            }
            data.add(Arr);
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    private List<String[]> getAmountData(List<String> dates, String customer, String[] columnArr) {
        StringBuilder selectionBuilder = new StringBuilder();
        List<String> list_SelectionArgs = new ArrayList<>();
        list_SelectionArgs.addAll(dates);
        selectionBuilder.append("payment_date IN (" + repeat("?", dates.size(), ", ") + ")");
        if (!customer.trim().isEmpty()) {
            selectionBuilder.append(" AND payment_customer = ?");
            list_SelectionArgs.add(customer);
        }

        String[] selectionArgs = list_SelectionArgs.toArray(new String[list_SelectionArgs.size()]);
        List<String[]> data = new ArrayList<>();
        Cursor cursor = dbManager.Payment_fetch(selectionBuilder.toString(), selectionArgs);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String[] Arr = new String[columnArr.length];
            for (int i = 0; i < Arr.length; i++) {
                Arr[i] = cursor.getString(cursor.getColumnIndexOrThrow(columnArr[i]));
            }
            data.add(Arr);
            cursor.moveToNext();
        }
        cursor.close();
        return data;
    }

    private List<String> giveAllDay(String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(change_date_format(startDateStr.trim()));
        LocalDate endDate = LocalDate.parse(change_date_format(endDateStr.trim()));
        List<String> dateStrings = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            dateStrings.add(startDate.format(formatter));
            startDate = startDate.plusDays(1);
        }
        return dateStrings;
    }

    private String change_date_format(String inputDateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(inputDateString, inputFormatter);
        String outputDateString = date.format(outputFormatter);
        return outputDateString;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed(); // Handle the back button click event
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, First_Activity.class);
        startActivity(i);
        Intent y = new Intent(this, on_click_btn_bill_list.class);
        startActivity(y);
    }
}