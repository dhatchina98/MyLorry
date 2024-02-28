package com.dcode.mylorry;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Bill_Adapter extends SimpleCursorAdapter {
    Context context;
    private final String billType;
    private boolean showCheckboxes = false;
    Set<String> selectedItems;

    private final List<CheckBox> itemList;
    private MenuStateChangeListener menuStateChangeListener;

    public Bill_Adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String billType ) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.billType = billType;
        itemList = new ArrayList<>();
        selectedItems = new HashSet<>();
    }

    public void setShowCheckboxes(boolean showCheckboxes) {
        this.showCheckboxes = showCheckboxes;
        notifyDataSetChanged();
    }

    public void selectAll(boolean boo){
        for (CheckBox item : itemList) {
            item.setChecked(boo);
        }
        notifyDataSetChanged();
    }

    public void updateCheckboxVisibility() {
        for (CheckBox item : itemList) {
            if (showCheckboxes) {
                item.setVisibility(View.VISIBLE);
            } else {
                item.setVisibility(View.GONE);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        TextView NameTextView = row.findViewById(R.id.file_name);
        TextView from_dateTextView = row.findViewById(R.id.bill_from_date);
        TextView to_dateTextView = row.findViewById(R.id.bill_to_date);
        String name = NameTextView.getText().toString();
        String from_date = from_dateTextView.getText().toString();
        String to_date = to_dateTextView.getText().toString();
        Button View_Button = row.findViewById(R.id.view);
        ImageView image = row.findViewById(R.id.bill_imageView);
        CheckBox cBox = row.findViewById(R.id.checkBox);
        if (itemList.isEmpty()) {
            itemList.add(cBox);
        } else if (!itemList.contains(cBox)) {
            itemList.add(cBox);
        }
        cBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cBox.isChecked()) {
                selectedItems.add(getFileName(name, from_date, to_date));
            } else {
                selectedItems.remove(getFileName(name, from_date, to_date));
            }
            System.out.println(selectedItems);
            if (selectedItems.isEmpty()) {
                setShowCheckboxes(false);
                updateCheckboxVisibility();
                if (menuStateChangeListener != null) {
                    menuStateChangeListener.onMenuStateChanged(false, new ArrayList<>(selectedItems));
                }
            } else {
                // Enable the menu item
                if (menuStateChangeListener != null) {
                    menuStateChangeListener.onMenuStateChanged(true, new ArrayList<>(selectedItems));
                }
            }
            notifyDataSetChanged();

        });
        // Set the background drawable based on the checkbox state
        int backgroundResId = R.drawable.bgimage_listitem;
        int view_bg = R.drawable.curved_white_button;
        int image_bg = R.mipmap.bill2_foreground;
        if (selectedItems.contains(getFileName(name, from_date, to_date))) {
            backgroundResId = R.drawable.bill_selected_view;
            view_bg = R.drawable.grey_background_for_view_btn;
            image_bg = R.mipmap.bill2_foreground;
        }
        row.setBackgroundResource(backgroundResId);
        View_Button.setBackgroundResource(view_bg);
        image.setImageResource(image_bg);
        row.setOnLongClickListener(view -> {
            Toast.makeText(context.getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
            cBox.setChecked(true);
            selectedItems.add(getFileName(name, from_date, to_date));
            setShowCheckboxes(true);
            updateCheckboxVisibility();
            return true;
        });
        View_Button.setOnClickListener(v -> {
            Intent intent = new Intent(context, PdfViewActivity.class);
            intent.putExtra("Pdf_fileName", getFileName(name, from_date, to_date));
            context.startActivity(intent);
        });

        return row;
    }

    private String getDateString_forFileName(String str) {
        String[] arr = str.split("/");
        return arr[0] + arr[1] + arr[2];
    }

    private String getFileName(String name, String from_date, String to_date) {
        String fileName = name + "_" + getDateString_forFileName(from_date) + "_" + getDateString_forFileName(to_date) + ".pdf";
        String str = billType + "/" + fileName;
        File file = new File(context.getExternalFilesDir(null), str);
//        File externalStorageDir = Environment.getExternalStorageDirectory();
//        File file = new File(externalStorageDir, "Documents/MyLorryStorage/"+str);
        return file.getAbsolutePath();
    }

    public interface MenuStateChangeListener {
        void onMenuStateChanged(boolean isEnabled, List<String> list);
    }

    public void setMenuStateChangeListener(MenuStateChangeListener listener) {
        this.menuStateChangeListener = listener;
    }
}

