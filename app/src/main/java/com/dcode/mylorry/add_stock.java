package com.dcode.mylorry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class add_stock extends AppCompatActivity implements View.OnClickListener {
    private EditText StockNameEditText;
    private EditText CountEditText;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stock);
        StockNameEditText = findViewById(R.id.stock_name);
        CountEditText = findViewById(R.id.stock_count);
        Button addTodoBtn = findViewById(R.id.btn_Add);
        //opening database
        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String StockName = StockNameEditText.getText().toString();
        final String Count = CountEditText.getText().toString();
        if(StockName.trim().isEmpty()){
            StockNameEditText.setError("Enter Product");
        }else if(Count.trim().isEmpty()){
            CountEditText.setError("Enter Unit");
        }else{
            dbManager.insert_stock(StockName, Count);
            Intent i = new Intent(this, on_click_btn_stock_list.class);
            startActivity(i);
        }
    }
}