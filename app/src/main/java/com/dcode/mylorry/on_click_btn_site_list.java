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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class on_click_btn_site_list extends AppCompatActivity implements Site_Adapter.MenuStateChangeListener {
    final String[] Values_from = new String[]{DataBaseHelper._ID,
            DataBaseHelper.SITE_NAME};

    final int[] Values_to = new int[]{R.id.site_id, R.id.site_name};
    private Site_Adapter adapter;

    private boolean isSelectAllClicked;
    private MenuItem delete_menu;
    private MenuItem cancel_menu;
    private MenuItem select_all_menu;
    boolean isItemEnabled = false;
    private List<String> selectedFiles;

    private ActionBar actionBar;
    private DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_list_view);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Sites");
        }
        //open database and fetch the data
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.site_fetch();
        ListView listView = findViewById(R.id.list_view);
        TextView NoOrders = findViewById(R.id.empty);
        NoOrders.setText(R.string.empty_site);
        NoOrders.setGravity(Gravity.CENTER);
        listView.setEmptyView(NoOrders);

        adapter = new Site_Adapter(this, R.layout.on_click_btn_site, cursor, Values_from, Values_to, 0);
        adapter.notifyDataSetChanged();
        adapter.setMenuStateChangeListener(this);
        listView.setAdapter(adapter);

//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.on_click_btn_site, cursor, Values_from, Values_to, 0);
//        adapter.notifyDataSetChanged();
//
//        listView.setAdapter(adapter);

        // OnCLickListener For List Items
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
//                TextView site_idTextView = view.findViewById(R.id.site_id);
//                TextView site_nameTextView = view.findViewById(R.id.site_name);
//
//                String site_id = site_idTextView.getText().toString();
//                String site_name = site_nameTextView.getText().toString();
//
//                Intent modify_intent = new Intent(getApplicationContext(), modify_site.class);
//                modify_intent.putExtra("id", site_id);
//                modify_intent.putExtra("name", site_name);
//                startActivity(modify_intent);
//            }
//        });
//        FloatingActionButton fab = findViewById(R.id.fab);
        Button fab = findViewById(R.id.down_add);
        Intent i = new Intent(getApplicationContext(), add_site.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(on_click_btn_site_list.this);
            builder.setMessage("Are you sure you want to delete?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Handle the action item click here
                boolean is_deleted = false;
                for (String str : selectedFiles) {
                    dbManager.delete_site(Long.parseLong(str));
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

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), on_click_btn_site_list.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    private void changeIcon() {
        int iconResId = isSelectAllClicked ? R.drawable.menu_selected_allchkbox_foreground : R.drawable.menu_select_allchkbox_foreground;
        select_all_menu.setIcon(iconResId);
    }
}