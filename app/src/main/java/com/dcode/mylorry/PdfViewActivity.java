package com.dcode.mylorry;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.File;

public class PdfViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("Pdf_fileName");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getName(fileName));
        }
        File file = new File(fileName);
        PDFView pdfView = findViewById(R.id.pdfImageView);
        pdfView.fromFile(file).load();
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
    private String getName(String str){
        String[] strArr=str.split("/");
        return strArr[strArr.length-1].split("_")[0]+" "+ strArr[strArr.length-1].split("_")[1];
    }
}
