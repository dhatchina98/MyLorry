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

public class modify_payment extends AppCompatActivity implements View.OnClickListener{
    private EditText dateEditText;
    private static AutoCompleteTextView customerTextView;
    private EditText amountEditText;
    private DBManager dbManager;
    Calendar calender;
    String[] arrCustomer;
    ArrayAdapter<String> CustomerAdapter;
    public String selected_Value;
    public static String selected_List;
    private long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_payment);
        calender = Calendar.getInstance();
        dbManager = new DBManager(this);
        dbManager.open();
        SQLiteDatabase db = dbManager.read();
        selected_Value = "";
        selected_List = "";
        dateEditText = findViewById(R.id.date);
        customerTextView = findViewById(R.id.customer);
        amountEditText = findViewById(R.id.amount);
        Button updateBtn = findViewById(R.id.btn_payment_update);
//        Button deleteBtn = findViewById(R.id.btn_delete);
        ImageButton dateBtn = findViewById(R.id.dateBtn);
        ImageButton customerBtn = findViewById(R.id.customerBtn);
        updateBtn.setOnClickListener(this);
//        deleteBtn.setOnClickListener(this);
        dateBtn.setOnClickListener(this);
        customerBtn.setOnClickListener(this);
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
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String date = intent.getStringExtra("date");
        String customer = intent.getStringExtra("name");
        String amount = intent.getStringExtra("amount");
        _id = Long.parseLong(id);
        dateEditText.setText(date);
        customerTextView.setText(customer);
        amountEditText.setText(amount);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_payment_update) {
            String date = dateEditText.getText().toString();
            String customer = customerTextView.getText().toString();
            String amount = amountEditText.getText().toString();
            if (date.trim().isEmpty()) {
                dateEditText.setError("Enter Date");
            } else if (customer.trim().isEmpty()) {
                customerTextView.setError("Enter Customer");
            } else if (amount.trim().isEmpty()) {
                amountEditText.setError("Enter Amount");
            } else {
                dbManager.update_payment(_id, date, customer, amount);
                this.returnHome();
            }
        }
//        else if (v.getId() == R.id.btn_delete) {
//            dbManager.delete_payment(_id);
//            this.returnHome();
//        }
        else if (v.getId() == R.id.dateBtn) {
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH);
            int day = calender.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(modify_payment.this, R.style.MyDatePickerDialogTheme,(datePicker, year_1, month_1, day_1) -> {
                String formattedDate = String.format(Locale.US, "%02d/%02d/%04d", day_1, month_1 + 1, year_1);
                dateEditText.setText(formattedDate);
            }, year, month, day).show();
        } else if (v.getId() == R.id.customerBtn) {
            selected_List = "Customer";
            call_ListView(arrCustomer, "Customer");
        }
    }

    public void call_ListView(String[] arr, String str) {
        String call_from = "modify_payment";
        Intent a = new Intent(getApplicationContext(), ListView_ImageBtn_AddOrder.class);
        a.putExtra("call_from", call_from);
        getArray(arr, str);
        startActivity(a);
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), on_click_btn_payment_list.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    public void getSelectedValue(String str) {
        selected_Value = str;
        customerTextView.setText(selected_Value);
    }
}