package com.dcode.mylorry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class modify_activity extends AppCompatActivity implements View.OnClickListener {

    private EditText NameEditText;
    private EditText MobileNoEditText;
    private DBManager dbManager;
    private long _id;
    private String call_from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_activity);

        dbManager = new DBManager(this);
        dbManager.open();
        NameEditText = findViewById(R.id.name);
        MobileNoEditText = findViewById(R.id.mobile_no);

        Button updateBtn = findViewById(R.id.btn_update);
//        Button deleteBtn = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        call_from = intent.getStringExtra("call_from");
        String id = intent.getStringExtra("id");
        String Name = intent.getStringExtra("name");
        String mobileNo = intent.getStringExtra("mobile_no");

        _id = Long.parseLong(id);
        NameEditText.setText(Name);
        MobileNoEditText.setText(mobileNo);
        updateBtn.setOnClickListener(this);
//        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                String name = NameEditText.getText().toString();
                String mobileNo = MobileNoEditText.getText().toString();
                if(name.trim().isEmpty()){
                    NameEditText.setError("Enter Name");
                }else if(mobileNo.trim().isEmpty()){
                    MobileNoEditText.setError("Enter MobileNo");
                }else{
                    if (call_from.equals("customer")) {
                        dbManager.update_customer(_id, name, mobileNo);
                    } else if (call_from.equals("vendor")) {
                        dbManager.update_vendor(_id, name, mobileNo);
                    }
                    this.returnHome(call_from);
                }
                break;
//            case R.id.btn_delete:
//                if (call_from.equals("customer")) {
//                    dbManager.delete_customer(_id);
//                } else if (call_from.equals("vendor")) {
//                    dbManager.delete_vendor(_id);
//                }
//                this.returnHome(call_from);
//                break;
        }
    }

    public void returnHome(String str) {
        if (str.equals("customer")) {
            Intent home_intent = new Intent(getApplicationContext(), on_click_btn_customer_list.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home_intent);
        } else if (str.equals("vendor")) {
            Intent home_intent = new Intent(getApplicationContext(), on_click_btn_vendor_list.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home_intent);
        }
    }
}