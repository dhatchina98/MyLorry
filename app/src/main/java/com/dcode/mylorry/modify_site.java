package com.dcode.mylorry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class modify_site extends AppCompatActivity implements View.OnClickListener {
    private EditText SiteNameEditText;
    private DBManager dbManager;
    private long _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_site);

        dbManager = new DBManager(this);
        dbManager.open();
        SiteNameEditText = findViewById(R.id.site_name);

        Button updateBtn = findViewById(R.id.btn_site_update);
//        Button deleteBtn = findViewById(R.id.btn_delete);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String SiteName = intent.getStringExtra("name");

        _id = Long.parseLong(id);
        SiteNameEditText.setText(SiteName);
        updateBtn.setOnClickListener(this);
//        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_site_update:
                String site_name = SiteNameEditText.getText().toString();
                if(site_name.trim().isEmpty()){
                    SiteNameEditText.setError("Enter Site");
                }else{
                    dbManager.update_site(_id, site_name);
                    this.returnHome();
                }
                break;
//            case R.id.btn_delete:
//                dbManager.delete_site(_id);
//                this.returnHome();
//                break;
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), on_click_btn_site_list.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);

    }
}