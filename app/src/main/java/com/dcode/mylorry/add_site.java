package com.dcode.mylorry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class add_site extends AppCompatActivity implements View.OnClickListener {
    private EditText SiteNameEditText;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_site);
        SiteNameEditText = findViewById(R.id.site_name);
        Button addTodoBtn = findViewById(R.id.btn_Add);
        //opening database
        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String Site_Name = SiteNameEditText.getText().toString();
        if(Site_Name.trim().isEmpty()){
            SiteNameEditText.setError("Enter Site");
        }else{
            dbManager.insert_site(Site_Name);
            Intent i = new Intent(this, on_click_btn_site_list.class);
            startActivity(i);
        }



    }
}