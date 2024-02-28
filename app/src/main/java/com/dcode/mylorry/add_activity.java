package com.dcode.mylorry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class add_activity extends AppCompatActivity implements View.OnClickListener {
    private EditText NameEditText;
    private EditText MobileNoEditText;
    private DBManager dbManager;
    private String call_from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        NameEditText = findViewById(R.id.name);
        MobileNoEditText = findViewById(R.id.mobile_no);
        Button addTodoBtn = findViewById(R.id.btn_Add);
        //opening database
        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
        Intent intent = getIntent();
        call_from = intent.getStringExtra("call_from");
    }

    @Override
    public void onClick(View view) {
        final String Name = NameEditText.getText().toString();
        final String mobilNo = MobileNoEditText.getText().toString();
        if(Name.trim().isEmpty()){
            NameEditText.setError("Enter Name");
        }else if(mobilNo.trim().isEmpty()){
            MobileNoEditText.setError("Enter MobileNo");
        }else{
            if (call_from.equals("customer")) {
                dbManager.insert_customer(Name,mobilNo);
                Intent i = new Intent(this, on_click_btn_customer_list.class);
                startActivity(i);
            } else if (call_from.equals("vendor")) {
                dbManager.insert_vendor(Name,mobilNo);
                Intent i = new Intent(this, on_click_btn_vendor_list.class);
                startActivity(i);
            }
        }


    }
}