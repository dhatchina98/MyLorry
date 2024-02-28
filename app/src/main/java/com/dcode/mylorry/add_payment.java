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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class add_payment extends AppCompatActivity implements View.OnClickListener {
    private EditText dateEditText;
    private static AutoCompleteTextView customerTextView;
    private EditText amountEditText;
    private int day;
    private int month;
    private int year;
    Calendar calender;
    String[] arrCustomer;
    ArrayAdapter<String> CustomerAdapter;
    public String selected_Value;
    public static String selected_List;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);
        selected_Value = "";
        selected_List = "";
        dateEditText = findViewById(R.id.date);
        customerTextView = findViewById(R.id.customer);
        amountEditText = findViewById(R.id.amount);
        Button addTodoBtn = findViewById(R.id.add_button);
        ImageButton dateBtn = findViewById(R.id.dateBtn);
        ImageButton customerBtn = findViewById(R.id.customerBtn);
        //opening database
        dbManager = new DBManager(this);
        dbManager.open();
        SQLiteDatabase db = dbManager.read();
        //OnClick Listener For Add Button
        addTodoBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        customerBtn.setOnClickListener(this);
        calender = Calendar.getInstance();
        //First Time Setting Current Date
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH);
        day = calender.get(Calendar.DAY_OF_MONTH);
        String day_temp = (day < 10) ? ("0" + day) : String.valueOf(day);
        String month_temp = ((month + 1) < 10) ? ("0" + (month + 1)) : String.valueOf(month + 1);
        dateEditText.setText(day_temp + "/" + month_temp + "/" + year);
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
    }

    //button onclick value operations
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_button) {
            final String date = dateEditText.getText().toString();
            final String customer = customerTextView.getText().toString();
            final String amount = amountEditText.getText().toString();
            if (date.trim().isEmpty()) {
                dateEditText.setError("Enter Date");
            } else if (customer.trim().isEmpty()) {
                customerTextView.setError("Enter Customer");
            } else if (amount.trim().isEmpty()) {
                amountEditText.setError("Enter Amount");
            } else {
                dbManager.insert_payment(date, customer, amount);
                Intent i = new Intent(this, on_click_btn_payment_list.class);
                startActivity(i);
            }
        } else if (v.getId() == R.id.dateBtn) {
            year = calender.get(Calendar.YEAR);
            month = calender.get(Calendar.MONTH);
            day = calender.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(add_payment.this,R.style.MyDatePickerDialogTheme, (datePicker, year, month, day) -> {
                String formattedDate = String.format(Locale.US, "%02d/%02d/%04d", day, month + 1, year);
                dateEditText.setText(formattedDate);
            }, year, month, day).show();
        } else if (v.getId() == R.id.customerBtn) {
            selected_List = "Customer";
            call_ListView(arrCustomer, "Customer");
        }
    }

    public void call_ListView(String[] arr, String str) {
        String call_from = "add_payment";
        Intent a = new Intent(getApplicationContext(), ListView_ImageBtn_AddOrder.class);
        a.putExtra("call_from", call_from);
        getArray(arr, str);
        startActivity(a);
    }

    public void getSelectedValue(String str) {
        selected_Value = str;
        customerTextView.setText(selected_Value);
    }
}