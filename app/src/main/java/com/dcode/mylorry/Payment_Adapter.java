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

public class Payment_Adapter extends SimpleCursorAdapter {
    private Context context;
    private boolean showCheckboxes = false;
    Set<String> selectedItems;

    private List<CheckBox> paymentList;
    private MenuStateChangeListener payment_menuStateChangeListener;

    public Payment_Adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        paymentList = new ArrayList<>();
        selectedItems = new HashSet<>();
    }

    public void setShowCheckboxes(boolean showCheckboxes) {
        this.showCheckboxes = showCheckboxes;
        notifyDataSetChanged();
    }

    public void selectAll(boolean boo) {
        for (CheckBox item : paymentList) {
            item.setChecked(boo);
        }
        notifyDataSetChanged();
    }

    public void updateCheckboxVisibility() {
        for (CheckBox item : paymentList) {
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
        CheckBox cBox = row.findViewById(R.id.payment_checkBox);
        TextView id = row.findViewById(R.id.payment_id);
        TextView name = row.findViewById(R.id.payment_customer_name);
        TextView date = row.findViewById(R.id.payment_date);
        TextView amount = row.findViewById(R.id.payment);
        ImageButton button = row.findViewById(R.id.payment_edit);
        if (paymentList.isEmpty()) {
            paymentList.add(cBox);
        } else if (!paymentList.contains(cBox)) {
            paymentList.add(cBox);
        }
        button.setOnClickListener(v -> {
            Intent modify_intent = new Intent(context, modify_payment.class);
            modify_intent.putExtra("id", id.getText().toString());
            modify_intent.putExtra("date", date.getText().toString());
            modify_intent.putExtra("name", name.getText().toString());
            modify_intent.putExtra("amount", amount.getText().toString());
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
                if (payment_menuStateChangeListener != null) {
                    payment_menuStateChangeListener.onMenuStateChanged(false, new ArrayList<>(selectedItems));
                }
            } else {
                // Enable the menu item
                if (payment_menuStateChangeListener != null) {
                    payment_menuStateChangeListener.onMenuStateChanged(true, new ArrayList<>(selectedItems));
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
        this.payment_menuStateChangeListener = listener;
    }
}
