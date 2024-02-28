package com.dcode.mylorry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class bill_folder extends AppCompatActivity implements Bill_Adapter.MenuStateChangeListener {

    final String[] Values_from = new String[]{"text", "from_date", "to_date"};
    final int[] Values_to = new int[]{R.id.file_name, R.id.bill_from_date, R.id.bill_to_date};
    private MenuItem delete_menu;
    private MenuItem cancel_menu;
    private MenuItem share_menu;
    private MenuItem select_all_menu;
    boolean isItemEnabled = false;

    private List<String> selectedFiles;
    private String billType;
    private Bill_Adapter adapter;

    private boolean isSelectAllClicked;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_listview);
        isSelectAllClicked = false;
        Intent intent = getIntent();
        billType = intent.getStringExtra("BillType");
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            String[] topic=billType.split("_");
            actionBar.setTitle(topic[0]+" "+topic[1]);
        }
        ListView listView = findViewById(R.id.list_view);
        adapter = new Bill_Adapter(this, R.layout.bill_folder, null, Values_from, Values_to, 0, billType);
        adapter.notifyDataSetChanged();
        adapter.setMenuStateChangeListener(this);
        adapter.swapCursor(showBills(billType));
        listView.setAdapter(adapter);
        if (adapter.getCount() == 0) {
            // The adapter has no items, navigate back to the previous activity
            go_back();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill_pdf_menu, menu);
        delete_menu = menu.findItem(R.id.delete_bill_menu);
        share_menu = menu.findItem(R.id.share_bill_menu);
        select_all_menu = menu.findItem(R.id.select_all_bill_menu);
        cancel_menu = menu.findItem(R.id.cancel_bill_menu);
        changeIcon();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        delete_menu.setEnabled(isItemEnabled);
        share_menu.setEnabled(isItemEnabled);
        if (isItemEnabled) {
            // Change the color of the menu item
            delete_menu.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            share_menu.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            select_all_menu.setVisible(true);
            cancel_menu.setVisible(true);
        } else {
            // Change the color of the menu item to gray
            delete_menu.getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            share_menu.getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            select_all_menu.setVisible(false);
            cancel_menu.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cancel_bill_menu) {
            adapter.selectAll(false);
        } else if (id == R.id.delete_bill_menu) {
            // Create a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(bill_folder.this);
            builder.setMessage("Are you sure you want to delete?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Handle the action item click here
                boolean is_deleted = false;
                for (String str : selectedFiles) {
                    is_deleted = new File(str).delete();
                }
                if (!is_deleted) {
                    Toast.makeText(this, "Can't Delete Bill(s)", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                returnHome();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            // Show the confirmation dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        } else if (id == R.id.select_all_bill_menu) {
            isSelectAllClicked = !isSelectAllClicked;
            changeIcon();
            adapter.selectAll(isSelectAllClicked);
            return true;
        } else if (id == R.id.share_bill_menu) {
            // Handle the action item click here
            shareFiles(selectedFiles);
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed(); // Handle the back button click event
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeIcon() {
        int iconResId = isSelectAllClicked ? R.drawable.menu_selected_allchkbox_foreground : R.drawable.menu_select_allchkbox_foreground;
        select_all_menu.setIcon(iconResId);
    }


    public void go_back() {
        Intent intent1 = new Intent(getApplicationContext(), First_Activity.class);
        startActivity(intent1);
        Intent intent = new Intent(getApplicationContext(), on_click_btn_bill_list.class);
        startActivity(intent);
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), bill_folder.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home_intent.putExtra("BillType", billType);
        startActivity(home_intent);
    }

    private void shareFiles(List<String> fileList) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("application/pdf"); // Set the MIME type to allow any file type
        ArrayList<Uri> fileUris = new ArrayList<>();
        for (String str : fileList) {
            File file = new File(str);
            System.out.println(str);
            Uri fileUri = FileProvider.getUriForFile(this, "com.dcode.mylorry.file_provider", file);
            fileUris.add(fileUri);
        }
//        for (String str : fileList) {
////            File file = new File(str);
//            System.out.println(str);
//            String[] arr =str.split("/");
//            String fileName =arr[6]+"/"+arr[7];
//            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MyLorryStorage/"+fileName);
//            System.out.println("@@@@@@@@ "+file.getAbsolutePath());
//            Uri fileUri = FileProvider.getUriForFile(this, "com.dcode.mylorry.file_provider", file);
//            fileUris.add(fileUri);
//        }
//        for (String str : fileList) {
//            File file = new File(str);
//            Uri uri = Uri.fromFile(file);
//            fileUris.add(uri);
//        }
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        shareIntent.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(shareIntent, "Share Bills via"));
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

    private Cursor showBills(String BillFolder) {
        String[] columns = {"_id", "text", "from_date", "to_date"};
        MatrixCursor cursor = new MatrixCursor(columns);
        File folder = new File(getExternalFilesDir(null), BillFolder);
//        File externalStorageDir = Environment.getExternalStorageDirectory();
//        File folder = new File(externalStorageDir, "Documents/MyLorryStorage/"+BillFolder);
        boolean boo = true;
        if (!folder.exists()) {
            boo = folder.mkdir();
        }
        if (folder.exists() && folder.isDirectory() && boo) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    Object[] rowData = {i, divideName(files[i].getName())[0], divideName(files[i].getName())[1], divideName(files[i].getName())[2]};
                    cursor.addRow(rowData);
                }
            }
        }
        return cursor;
    }

    private String[] divideName(String file) {
        String[] out = new String[3];
        String[] arr = file.split("_");
        out[0] = arr[0]+"_"+arr[1];
        String date1 = arr[2].trim();
        String date2 = arr[3].trim();
        out[1] = date1.substring(0, 2) + "/" + date1.substring(2, 4) + "/" + date1.substring(4, 8);
        out[2] = date2.substring(0, 2) + "/" + date2.substring(2, 4) + "/" + date2.substring(4, 8);
        return out;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, on_click_btn_bill_list.class);
        startActivity(i);
    }

}