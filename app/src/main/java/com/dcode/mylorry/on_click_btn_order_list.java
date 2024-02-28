package com.dcode.mylorry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class on_click_btn_order_list extends AppCompatActivity implements order_list_adapter.MenuStateChangeListener {

    final String[] Values_from = new String[]{DataBaseHelper._ID,
            DataBaseHelper.DATE, DataBaseHelper.FROM, DataBaseHelper.PRODUCT, DataBaseHelper.CUSTOMER, DataBaseHelper.SITE, DataBaseHelper.QUANTITY, DataBaseHelper.UNIT, DataBaseHelper.AMOUNT, DataBaseHelper.AMOUNT_MODE, DataBaseHelper.VEHICLE_NO};

    final int[] Values_to = new int[]{R.id.id, R.id.date, R.id.from, R.id.product, R.id.customer, R.id.site, R.id.quantity, R.id.unit, R.id.amount, R.id.amount_mode, R.id.vehicle_no};
    boolean isItemEnabled = false;
    private List<String> selectedFiles;
    private boolean isSelectAllClicked;
    private ActionBar actionBar;
    private MenuItem delete_menu;
    private MenuItem cancel_menu;
    private MenuItem select_all_menu;
    private DBManager dbManager;
    private order_list_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_order_list_view);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Orders");
        }
        //open database and fetch the data
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        ListView listView = (ListView) findViewById(R.id.list_view_order);
        TextView NoOrders = findViewById(R.id.empty_order);
        NoOrders.setText(R.string.empty_order);
        NoOrders.setGravity(Gravity.CENTER);
        listView.setEmptyView(NoOrders);
        adapter = new order_list_adapter(this, R.layout.on_click_btn_order, cursor, Values_from, Values_to, 0);
        adapter.notifyDataSetChanged();
        adapter.setMenuStateChangeListener(this);
        listView.setAdapter(adapter);

        // OnCLickListener For List Items
//        Intent j=new Intent(this,show_order.class);
        //                startActivity(j);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
//                TextView idTextView = view.findViewById(R.id.id);
//                TextView dateTextView = view.findViewById(R.id.date);
//                TextView fromTextView = view.findViewById(R.id.from);
//                TextView productTextView = view.findViewById(R.id.product);
//                TextView customerTextView = view.findViewById(R.id.customer);
//                TextView siteTextView = view.findViewById(R.id.site);
//                TextView quantityTextView = view.findViewById(R.id.quantity);
//                TextView unitTextView = view.findViewById(R.id.unit);
//                TextView amountTextView = view.findViewById(R.id.amount);
//                TextView amount_modeTextView = view.findViewById(R.id.amount_mode);
//                TextView vehicle_noTextView = view.findViewById(R.id.vehicle_no);
//
//                String id = idTextView.getText().toString();
//                String date = dateTextView.getText().toString();
//                String from = fromTextView.getText().toString();
//                String product = productTextView.getText().toString();
//                String[] arr = product.split(" \\(");
//                String customer = customerTextView.getText().toString();
//                String site = siteTextView.getText().toString();
//                String quantity = quantityTextView.getText().toString();
//                String unit = unitTextView.getText().toString();
//                String amount = amountTextView.getText().toString();
//                String amount_mode = amount_modeTextView.getText().toString();
//                String vehicle_no = vehicle_noTextView.getText().toString();
//
//                Intent modify_intent = new Intent(getApplicationContext(), modify_order.class);
//                modify_intent.putExtra("id", id);
//                modify_intent.putExtra("date", date);
//                modify_intent.putExtra("from", from);
//                modify_intent.putExtra("product", arr[0]);
//                modify_intent.putExtra("quantity", quantity);
//                modify_intent.putExtra("customer", customer);
//                modify_intent.putExtra("site", site);
//                modify_intent.putExtra("unit", unit);
//                modify_intent.putExtra("amount", amount);
//                modify_intent.putExtra("amount_mode", amount_mode);
//                modify_intent.putExtra("vehicle_no", vehicle_no);
//                startActivity(modify_intent);
//            }
//        });
//        FloatingActionButton fab = findViewById(R.id.fab);
        Button fab = findViewById(R.id.down_add_order);
        Intent i = new Intent(this, add_order.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        delete_menu = menu.findItem(R.id.delete_menu);
        select_all_menu = menu.findItem(R.id.select_all_menu);
        cancel_menu = menu.findItem(R.id.cancel_menu);
        changeIcon();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        delete_menu.setEnabled(isItemEnabled);
        if (isItemEnabled) {
            // Change the color of the menu item
            delete_menu.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            select_all_menu.setVisible(true);
            cancel_menu.setVisible(true);
        } else {
            // Change the color of the menu item to gray
            delete_menu.getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            select_all_menu.setVisible(false);
            cancel_menu.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, First_Activity.class);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cancel_menu) {
            adapter.selectAll(false);
        } else if (id == android.R.id.home) {
            onBackPressed(); // Handle the back button click event
            return true;
        } else if (id == R.id.delete_menu) {
            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(on_click_btn_order_list.this);
            builder.setMessage("Are you sure you want to delete?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Handle the action item click here
                boolean is_deleted = false;
                for (String str : selectedFiles) {
                    dbManager.delete(Long.parseLong(str));
                    is_deleted = true;
                }
                if (!is_deleted) {
                    Toast.makeText(this, "Can't Delete Entries", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                returnHome();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            // Show the confirmation dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        } else if (id == R.id.select_all_menu) {
            isSelectAllClicked = !isSelectAllClicked;
            changeIcon();
            adapter.selectAll(isSelectAllClicked);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuStateChanged(boolean isEnabled, List<String> list) {
        // Enable/Disable the menu item
        isItemEnabled = isEnabled;
        invalidateOptionsMenu();
        if (isEnabled) {
            selectedFiles = list;
            actionBar.setDisplayShowTitleEnabled(false);
        } else {
            isSelectAllClicked = false;
            actionBar.setDisplayShowTitleEnabled(true);
            changeIcon();
        }
    }

    private void changeIcon() {
        int iconResId = isSelectAllClicked ? R.drawable.menu_selected_allchkbox_foreground : R.drawable.menu_select_allchkbox_foreground;
        select_all_menu.setIcon(iconResId);
    }
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), on_click_btn_order_list.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
}