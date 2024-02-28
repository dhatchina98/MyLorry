package com.dcode.mylorry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

public class on_click_btn_bill_list extends AppCompatActivity {
    final String[] Values_from = new String[]{"text", "count"};

    final int[] Values_to = new int[]{R.id.folder_name, R.id.bill_count};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_list_view);
        Button add = findViewById(R.id.down_add);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Bills");
        }
        ListView listView = findViewById(R.id.list_view);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.on_click_btn_bill_list, null, Values_from, Values_to, 0);
        adapter.swapCursor(createCursorFromList());
        listView.setAdapter(adapter);
        // OnCLickListener For List Items
        listView.setOnItemClickListener((parent, view, position, viewId) -> {
            //background color change
            ImageView image =view.findViewById(R.id.folderType_imageView);
            TextView BillCount_TextView = view.findViewById(R.id.bill_count);
            String BillCount = BillCount_TextView.getText().toString();
            TextView BillType_TextView = view.findViewById(R.id.folder_name);
            String BillType = BillType_TextView.getText().toString();
            if (Integer.parseInt(BillCount) > 0) {
//                image.setImageResource(R.mipmap.folder_grey);
                view.setBackgroundResource(R.drawable.bill_selected_view);
                Intent i = new Intent(getApplicationContext(), bill_folder.class);
                i.putExtra("BillType", BillType + "_Bills");
                startActivity(i);
            } else {
                Toast.makeText(on_click_btn_bill_list.this, "No " + BillType + " Bills", Toast.LENGTH_SHORT).show();
            }
        });
        Intent i = new Intent(getApplicationContext(), make_bill.class);
        add.setOnClickListener(view -> startActivity(i));
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, First_Activity.class);
        startActivity(i);
    }

    private Cursor createCursorFromList() {
        String[] columns = {"_id", "text", "count"};
        MatrixCursor cursor = new MatrixCursor(columns);
        Object[] rowData = {0, "Customer", findCount("Customer")};
        cursor.addRow(rowData);
        Object[] rowData1 = {1, "Vendor", findCount("Vendor")};
        cursor.addRow(rowData1);
        Object[] rowData2 = {2, "Order", findCount("Order")};
        cursor.addRow(rowData2);

        return cursor;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed(); // Handle the back button click event
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String findCount(String billType) {
        String no = "0";
      File folder = new File(getExternalFilesDir(null), billType + "_Bills");
//        File externalStorageDir = Environment.getExternalStorageDirectory();
//        File folder = new File(externalStorageDir, "Documents/MyLorryStorage/"+billType + "_Bills");
        boolean boo = true;
        if (!folder.exists()) {
            boo = folder.mkdirs();
        }
        if (folder.exists() && folder.isDirectory() && boo) {
            File[] files = folder.listFiles();
            if (files != null) {
                no = String.valueOf(files.length);
            }
        }
        return no;
    }


}