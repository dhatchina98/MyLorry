package com.dcode.mylorry;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListView_ImageBtn_AddOrder extends AppCompatActivity {
    static String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_image_btn_add_order);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, R.layout.textview_for_list_imagebtn, items);
        ListView listView =findViewById(R.id.imgBtn_list_view);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener((parent, view, position, viewId) -> {
            String selected_Value = listView.getItemAtPosition(position).toString();
            for (int i = 0; i < listView.getChildCount(); i++) {
                if (position == i) {
                    listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.purple_500));
                } else {
                    listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
            }
            if(selected_Value.equals("+ Add Vendor")){
                Intent j = new Intent(this, on_click_btn_vendor_list.class);
                startActivity(j);
                Intent i = new Intent(getApplicationContext(), add_activity.class);
                i.putExtra("call_from", "vendor");
                startActivity(i);
            }else if(selected_Value.equals("+ Add Product")){
                Intent j = new Intent(this, on_click_btn_stock_list.class);
                startActivity(j);
                Intent k = new Intent(this, add_stock.class);
                startActivity(k);
            }else if(selected_Value.equals("+ Add Customer")){
                Intent j = new Intent(this, on_click_btn_customer_list.class);
                startActivity(j);
                Intent i = new Intent(getApplicationContext(), add_activity.class);
                i.putExtra("call_from", "customer");
                startActivity(i);
            }else if(selected_Value.equals("+ Add Site")){
                Intent j = new Intent(this, on_click_btn_site_list.class);
                startActivity(j);
                Intent k = new Intent(this, add_site.class);
                startActivity(k);
            }else if(selected_Value.equals("+ Add Vehicle")){
                Intent j = new Intent(this, on_click_btn_vehicle_list.class);
                startActivity(j);
                Intent k = new Intent(this, add_vehicle.class);
                startActivity(k);
            } else{
                Intent intent = getIntent();
                String call_from = intent.getStringExtra("call_from");
                if (call_from.equals("add")) {
                    new add_order().getSelectedValue(selected_Value);
                } else if (call_from.equals("modify")) {
                    new modify_order().getSelectedValue(selected_Value);
                }else if (call_from.equals("bill")) {
                    new make_bill().getSelectedValue(selected_Value);
                }else if (call_from.equals("add_payment")) {
                    new add_payment().getSelectedValue(selected_Value);
                } else if (call_from.equals("modify_payment")) {
                    new modify_payment().getSelectedValue(selected_Value);
                }
            }
            finish();
        });
    }

    public static void getArray(String[] arr,String str) {
        if(str.equals("none")){
            items = new String[arr.length ];
            for (int i = 0; i < arr.length; i++) {
                items[i] = arr[i];
            }
        }else{
            items = new String[arr.length + 1];
            items[0] = "+ Add "+str;
            for (int i = 1; i <= arr.length; i++) {
                items[i] = arr[i - 1];
            }
        }
    }
}
