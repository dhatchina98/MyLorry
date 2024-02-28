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

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class order_list_adapter extends SimpleCursorAdapter {
    private Context context;
    private boolean showCheckboxes = false;
    Set<String> selectedItems;
    private List<CheckBox> orderList;
    private MenuStateChangeListener order_menuStateChangeListener;

    public order_list_adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context=context;
        orderList = new ArrayList<>();
        selectedItems = new HashSet<>();
    }
    public void setShowCheckboxes(boolean showCheckboxes) {
        this.showCheckboxes = showCheckboxes;
        notifyDataSetChanged();
    }

    public void selectAll(boolean boo){
        for (CheckBox item : orderList) {
            item.setChecked(boo);
        }
        notifyDataSetChanged();
    }

    public void updateCheckboxVisibility() {
        for (CheckBox item : orderList) {
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
        TextView productTextView = row.findViewById(R.id.product);
        TextView quantityTextView = row.findViewById(R.id.quantity);
        TextView unitTextView = row.findViewById(R.id.unit);
        TextView txt =row.findViewById(R.id.amount_mode);
        TextView id = row.findViewById(R.id.id);
        productTextView.setText(productTextView.getText().toString()+" ( "+quantityTextView.getText().toString()+" "+unitTextView.getText().toString()+" )");
        String value=txt.getText().toString();
        if(value.equals("Cash"))
        {
            row.setBackground(ContextCompat.getDrawable(context, R.drawable.green_box));
        }
        else
        {
            row.setBackground(ContextCompat.getDrawable(context, R.drawable.red_box));
        }
        CheckBox cBox = row.findViewById(R.id.order_checkBox);
        ImageButton button = row.findViewById(R.id.order_edit);
        if (orderList.isEmpty()) {
            orderList.add(cBox);
        } else if (!orderList.contains(cBox)) {
            orderList.add(cBox);
        }
        button.setOnClickListener(v -> {
            TextView dateTextView = row.findViewById(R.id.date);
            TextView fromTextView = row.findViewById(R.id.from);
            TextView customerTextView = row.findViewById(R.id.customer);
            TextView siteTextView = row.findViewById(R.id.site);
            TextView amountTextView1 = row.findViewById(R.id.amount);
            TextView vehicle_noTextView = row.findViewById(R.id.vehicle_no);

            Intent modify_intent = new Intent(context, modify_order.class);
            modify_intent.putExtra("id", id.getText().toString());
            modify_intent.putExtra("date",  dateTextView.getText().toString());
            modify_intent.putExtra("from", fromTextView.getText().toString());
            modify_intent.putExtra("product", productTextView.getText().toString().split(" \\(")[0]);
            modify_intent.putExtra("quantity", quantityTextView.getText().toString());
            modify_intent.putExtra("customer",  customerTextView.getText().toString());
            modify_intent.putExtra("site", siteTextView.getText().toString());
            modify_intent.putExtra("unit", unitTextView.getText().toString());
            modify_intent.putExtra("amount", amountTextView1.getText().toString());
            modify_intent.putExtra("amount_mode", txt.getText().toString());
            modify_intent.putExtra("vehicle_no", vehicle_noTextView.getText().toString());
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
                if (order_menuStateChangeListener != null) {
                    order_menuStateChangeListener.onMenuStateChanged(false, new ArrayList<>(selectedItems));
                }
            } else {
                // Enable the menu item
                if (order_menuStateChangeListener != null) {
                    order_menuStateChangeListener.onMenuStateChanged(true, new ArrayList<>(selectedItems));
                }
            }
            notifyDataSetChanged();
        });
        // Set the background drawable based on the checkbox state
        if (selectedItems.contains(id.getText().toString())) {
            int backgroundResId = R.drawable.bill_selected_view;
            row.setBackgroundResource(backgroundResId);
        }
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
        this.order_menuStateChangeListener = listener;
    }

}

//package com.dcode.mylorry;
//
//        import android.content.Context;
//        import android.database.Cursor;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.SimpleCursorAdapter;
//        import android.widget.TextView;
//        import androidx.core.content.ContextCompat;
//
//public class order_list_adapter extends SimpleCursorAdapter {
//    Context context;
//    public order_list_adapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
//        super(context, layout, c, from, to, flags);
//        this.context=context;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View row = super.getView(position, convertView, parent);
//        TextView productTextView = row.findViewById(R.id.product);
//        TextView quantityTextView = row.findViewById(R.id.quantity);
//        TextView unitTextView = row.findViewById(R.id.unit);
//        TextView txt =row.findViewById(R.id.amount_mode);
//        productTextView.setText(productTextView.getText().toString()+" ( "+quantityTextView.getText().toString()+" "+unitTextView.getText().toString()+" )");
//        String value=txt.getText().toString();
//        if(value.equals("Cash"))
//        {
//            row.setBackground(ContextCompat.getDrawable(context, R.drawable.green_box));
//        }
//        else
//        {
//            row.setBackground(ContextCompat.getDrawable(context, R.drawable.red_box));
//        }
//        return row;
//    }
//}
