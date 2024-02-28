package com.dcode.mylorry;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.dcode.mylorry.databinding.FirstActivityBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class First_Activity extends AppCompatActivity {

    private FirstActivityBinding binding;
    private static final int REQUEST_PICK_FILE = 1;
    NavigationView navigationView;
    private int selectedNavItem;

    private DBManager dbManager;
    private TextView vehicleTextView;
    private TextView siteTextView;
    private TextView orderTextView;
    private TextView customerTextView;
    private TextView productTextView;
    private TextView earnedTextView;
    private TextView balanceTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FirstActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarFirst.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
        selectedNavItem = R.id.nav_home;
        selectHome();

        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_backup:
                        shareToGoogleDrive(copyDataBaseFile());
                        return true;
                    case R.id.nav_restore:
                        showFileChooser();
                        return true;
                    case R.id.nav_send_to_frnds:
                        String linkToShare = "https://play.google.com/store/apps/details?id=" + getPackageName();
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, linkToShare);
                        startActivity(Intent.createChooser(shareIntent, "Share Link"));
                        return true;
                    case R.id.nav_contact_us:
                        String email = "dcodeappsofficial@gmail.com";
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + email));
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Set up the drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, binding.appBarFirst.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ///////////////////////////////////////
        vehicleTextView = findViewById(R.id.pf_vehicle);
        siteTextView = findViewById(R.id.pf_sites);
        orderTextView = findViewById(R.id.pf_order);
        customerTextView = findViewById(R.id.pf_customer);
        productTextView = findViewById(R.id.pf_product);
        earnedTextView = findViewById(R.id.pf_earned);
        balanceTextView = findViewById(R.id.pf_balance);
        //open database and fetch the data
        dbManager = new DBManager(this);
        dbManager.open();
        //Fill PortFolio Tab
        fillPortFolio();
    }

    void fillPortFolio(){
        vehicleTextView.setText(String.valueOf(dbManager.Vehicle_Count()));
        siteTextView.setText(String.valueOf(dbManager.Site_Count()));
        orderTextView.setText(String.valueOf(dbManager.Order_Count()));
        customerTextView.setText(String.valueOf(dbManager.Customer_Count()));
        productTextView.setText(String.valueOf(dbManager.Product_Count()));
        earnedTextView.setText("₹ "+String.valueOf(dbManager.Cash_Total())+".00");
        balanceTextView.setText("₹ "+String.valueOf(dbManager.Credit_total())+".00");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore the selected state when returning to the home section
        MenuItem selectedItem = navigationView.getMenu().findItem(selectedNavItem);
        if (selectedItem != null) {
            selectedItem.setChecked(true);
        }
        fillPortFolio();
    }

    private void selectHome() {
        MenuItem selectedItem = navigationView.getMenu().findItem(R.id.nav_home);
        if (selectedItem != null) {
            selectedItem.setChecked(true);
        }
    }

    public void OnClickOfBillsButton(View v) {
        Intent i = new Intent(this, on_click_btn_bill_list.class);
        startActivity(i);
    }

    public void OnClickOfOrderButton(View v) {
        Intent i = new Intent(this, on_click_btn_order_list.class);
        startActivity(i);
    }

    public void OnClickOfCustomerButton(View v) {
        Intent i = new Intent(this, on_click_btn_customer_list.class);
        startActivity(i);
    }

    public void OnClickOfVendorsButton(View v) {
        Intent i = new Intent(this, on_click_btn_vendor_list.class);
        startActivity(i);
    }

    public void OnClickOfSiteButton(View v) {
        Intent i = new Intent(this, on_click_btn_site_list.class);
        startActivity(i);
    }

    public void OnClickOfStockButton(View v) {
        Intent i = new Intent(this, on_click_btn_stock_list.class);
        startActivity(i);
    }

    public void OnClickOfVehicleButton(View v) {
        Intent i = new Intent(this, on_click_btn_vehicle_list.class);
        startActivity(i);
    }

    public void OnClickOfPaymentsButton(View v) {
        Intent i = new Intent(this, on_click_btn_payment_list.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(First_Activity.this);
        builder.setMessage("Are you sure you want to exit ?");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_, menu);
        return true;
    }

    private File copyDataBaseFile() {
        // Open the existing database
        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                this.getDatabasePath("my_lorry.DB").getPath(),
                null,
                SQLiteDatabase.OPEN_READONLY);

        File folder = new File(this.getExternalFilesDir(null), "Database_Backup");
//        File externalStorageDir = Environment.getExternalStorageDirectory();
//        File folder = new File(externalStorageDir, "Documents/MyLorryStorage/Database_Backup");

        if (!folder.exists()) {
            folder.mkdirs();
        }
        // Get the current date
        Date currentDate = new Date();

        // Define the date format you want
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        // Format the current date
        String formattedDate = dateFormat.format(currentDate);

        // Create a backup file in the app's external storage directory
        File backupFile = new File(this.getExternalFilesDir(null), "Database_Backup/my_lorry_backup_" + formattedDate + ".DB");
//        File backupFile = new File(externalStorageDir, "Documents/MyLorryStorage/Database_Backup/my_lorry_backup_" + formattedDate + ".DB");
        // Copy the database to the backup file
        try (InputStream inputStream = new FileInputStream(db.getPath());
             OutputStream outputStream = new FileOutputStream(backupFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Close the database
        db.close();
        return backupFile;
    }

    private void shareToGoogleDrive(File databaseFile) {
//        selectHome();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/vnd.sqlite3"); // Set the MIME type to allow any file type
        Uri fileUri = FileProvider.getUriForFile(this, "com.dcode.mylorry.file_provider", databaseFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setPackage("com.google.android.apps.docs");
        startActivity(Intent.createChooser(shareIntent, "Store Backup in Drive"));
    }
//    private String copyDataBaseFile() {
//        // Open the existing database
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(
//                this.getDatabasePath("my_lorry.DB").getPath(),
//                null,
//                SQLiteDatabase.OPEN_READONLY);
//
//        File folder = new File(this.getExternalFilesDir(null), "Database_Backup");
////        File externalStorageDir = Environment.getExternalStorageDirectory();
////        File folder = new File(externalStorageDir, "Documents/MyLorryStorage/Database_Backup");
//
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        // Get the current date
//        Date currentDate = new Date();
//
//        // Define the date format you want
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//
//        // Format the current date
//        String formattedDate = dateFormat.format(currentDate);
//
//        // Create a backup file in the app's external storage directory
//        File backupFile = new File(this.getExternalFilesDir(null), "Database_Backup/my_lorry_backup_" + formattedDate + ".DB");
////        File backupFile = new File(externalStorageDir, "Documents/MyLorryStorage/Database_Backup/my_lorry_backup_" + formattedDate + ".DB");
//        // Copy the database to the backup file
//        try (InputStream inputStream = new FileInputStream(db.getPath());
//             OutputStream outputStream = new FileOutputStream(backupFile)) {
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) > 0) {
//                outputStream.write(buffer, 0, length);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // Close the database
//        db.close();
//        return "my_lorry_backup_" + formattedDate + ".DB";
//    }
//    private void shareToGoogleDrive(String databaseFile) {
////        selectHome();
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("application/vnd.sqlite3"); // Set the MIME type to allow any file type
//        File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MyLorryStorage/Database_Backup/"+databaseFile);
//        Uri fileUri = FileProvider.getUriForFile(this, "com.dcode.mylorry.file_provider", file);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
//        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        shareIntent.setPackage("com.google.android.apps.docs");
//        startActivity(Intent.createChooser(shareIntent, "Store Backup in Drive"));
//    }

    private void showFileChooser() {
//        selectHome();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.sqlite3");
        startActivityForResult(intent, REQUEST_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedFileUri = data.getData();
                handleSelectedFile(selectedFileUri);
            }
        }
    }

    private void handleSelectedFile(Uri fileUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File selectedFile = UriToFileUtil.uriToFile(this, fileUri);
            if (selectedFile != null) {
                restoreDatabase(selectedFile);
            }
        } else {
            File selectedFile = new File(fileUri.getPath());
            if (selectedFile.exists()) {
                restoreDatabase(selectedFile);
            }
        }
    }

    private void restoreDatabase(File backupDB) {
        try {
            File currentDB = getDatabasePath("my_lorry.DB");
            if (backupDB.exists()) {
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(this, "Database restore successful.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No backup found.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Database restore failed.", Toast.LENGTH_SHORT).show();
        }
    }


}