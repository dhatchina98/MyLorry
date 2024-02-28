package com.dcode.mylorry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class add_vehicle extends AppCompatActivity implements View.OnClickListener {
    private EditText VehicleNameEditText;
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);
        VehicleNameEditText = findViewById(R.id.vehicle_name);
        Button addTodoBtn = findViewById(R.id.btn_Add);
        //opening database
        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String Vehicle_Name = VehicleNameEditText.getText().toString();
        if(!Vehicle_Name.trim().isEmpty()){
            dbManager.insert_vehicle(Vehicle_Name);
            Intent i = new Intent(this, on_click_btn_vehicle_list.class);
            startActivity(i);
        }else{
            VehicleNameEditText.setError("Enter Vehicle No");
        }
    }
}