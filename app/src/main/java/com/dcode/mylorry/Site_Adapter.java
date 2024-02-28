package com.dcode.mylorry;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Site_Adapter extends SimpleCursorAdapter {
    Context context;
    private boolean showCheckboxes = false;
    private Set<String> selectedItems;

    private List<CheckBox> siteList;
    private MenuStateChangeListener site_menuStateChangeListener;

    public Site_Adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        siteList = new ArrayList<>();
        selectedItems = new HashSet<>();
    }

    public void setShowCheckboxes(boolean showCheckboxes) {
        this.showCheckboxes = showCheckboxes;
        notifyDataSetChanged();
    }

    public void selectAll(boolean boo){
        for (CheckBox item : siteList) {
            item.setChecked(boo);
        }
        notifyDataSetChanged();
    }

    public void updateCheckboxVisibility() {
        for (CheckBox item : siteList) {
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
        CheckBox cBox = row.findViewById(R.id.site_checkBox);
        TextView site_name = row.findViewById(R.id.site_name);
        ImageButton button = row.findViewById(R.id.site_edit);
        TextView id = row.findViewById(R.id.site_id);
        if (siteList.isEmpty()) {
            siteList.add(cBox);
        } else if (!siteList.contains(cBox)) {
            siteList.add(cBox);
        }
        button.setOnClickListener(v -> {
            Intent modify_intent = new Intent(context, modify_site.class);
            modify_intent.putExtra("id", id.getText().toString());
            modify_intent.putExtra("name", site_name.getText().toString());
            context.startActivity(modify_intent);
        });
        cBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cBox.isChecked()) {
                selectedItems.add(id.getText().toString());
            } else {
                selectedItems.remove(id.getText().toString());
            }
            if (selectedItems.isEmpty()) {
                setShowCheckboxes(false);
                updateCheckboxVisibility();
                if (site_menuStateChangeListener != null) {
                    site_menuStateChangeListener.onMenuStateChanged(false, new ArrayList<>(selectedItems));
                }
            } else {
                // Enable the menu item
                if (site_menuStateChangeListener != null) {
                    site_menuStateChangeListener.onMenuStateChanged(true, new ArrayList<>(selectedItems));
                }
            }
            notifyDataSetChanged();
        });
        // Set the background drawable based on the checkbox state
        int backgroundResId = R.drawable.bgimage_listitem;
        if (selectedItems.contains(id.getText().toString())) {
            backgroundResId = R.drawable.bill_selected_view;
        }
        row.setBackgroundResource(backgroundResId);
        row.setOnLongClickListener(view -> {
            Toast.makeText(context.getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
            cBox.setChecked(true);
            selectedItems.add(id.getText().toString());
            setShowCheckboxes(true);
            updateCheckboxVisibility();
            return true;
        });
        return row;
    }

    public interface MenuStateChangeListener {
        void onMenuStateChanged(boolean isEnabled, List<String> list);
    }

    public void setMenuStateChangeListener(MenuStateChangeListener listener) {
        this.site_menuStateChangeListener = listener;
    }
}
