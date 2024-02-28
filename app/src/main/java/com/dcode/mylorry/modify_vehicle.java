package com.dcode.mylorry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class modify_vehicle extends AppCompatActivity implements View.OnClickListener{
    private EditText VehicleNameEditText;
    private DBManager dbManager;
    private long _id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_vehicle);

        dbManager = new DBManager(this);
        dbManager.open();
        VehicleNameEditText = findViewById(R.id.vehicle_name);

        Button updateBtn = findViewById(R.id.btn_vehicle_update);
//        Button deleteBtn = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String VehicleName = intent.getStringExtra("name");

        _id = Long.parseLong(id);
        VehicleNameEditText.setText(VehicleName);
        updateBtn.setOnClickListener(this);
//        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_vehicle_update:
                String vehicle_name = VehicleNameEditText.getText().toString();
                if(!vehicle_name.trim().isEmpty()) {
                    dbManager.update_vehicle(_id, vehicle_name);
                    this.returnHome();
                }else{
                    VehicleNameEditText.setError("Enter Vehicle No");
                }
                break;
//            case R.id.btn_delete:
//                dbManager.delete_vehicle(_id);
//                this.returnHome();
//                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), on_click_btn_vehicle_list.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);

    }
}