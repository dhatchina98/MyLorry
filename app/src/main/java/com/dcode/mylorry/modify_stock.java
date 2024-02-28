package com.dcode.mylorry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class modify_stock extends AppCompatActivity implements View.OnClickListener {
    private EditText StockNameEditText;
    private EditText CountEditText;
    private DBManager dbManager;
    private long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_stock);
        dbManager = new DBManager(this);
        dbManager.open();
        StockNameEditText = findViewById(R.id.product_name);
        CountEditText = findViewById(R.id.product_unit);

        Button updateBtn = findViewById(R.id.btn_product_update);
//        Button deleteBtn = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String Name = intent.getStringExtra("name");
        String Count = intent.getStringExtra("count");

        _id = Long.parseLong(id);
        StockNameEditText.setText(Name);
        CountEditText.setText(Count);
        updateBtn.setOnClickListener(this);
//        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_product_update:
                String name = StockNameEditText.getText().toString();
                String count = CountEditText.getText().toString();
                if(name.trim().isEmpty()){
                    StockNameEditText.setError("Enter Product");
                }else if(count.trim().isEmpty()){
                    CountEditText.setError("Enter unit");
                }else{
                    dbManager.update_stock(_id, name, count);
                    this.returnHome();
                }
                break;
//            case R.id.btn_delete:
//                dbManager.delete_stock(_id);
//                this.returnHome();
//                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), on_click_btn_stock_list.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);

    }
}