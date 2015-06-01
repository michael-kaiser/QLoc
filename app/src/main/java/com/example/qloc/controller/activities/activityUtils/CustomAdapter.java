package com.example.qloc.controller.activities.activityUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qloc.R;

import java.util.List;

/**
 * Adapter for the ListView of the PlayGameActivity
 * @author michael
 */
public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<RowItem> rowItems;

    public CustomAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public RowItem getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    /* private view holder class */
    private class ViewHolder {
        TextView desc;
        TextView location;
        TextView distance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_layout, null);
            holder = new ViewHolder();

            holder.desc = (TextView) convertView
                    .findViewById(R.id.desc);
            holder.location = (TextView) convertView
                    .findViewById(R.id.location);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);

            RowItem row_pos = rowItems.get(position);

            holder.desc.setText(row_pos.getDesc());
            holder.location.setText(row_pos.getLocation());
            holder.distance.setText(row_pos.getDistance());

            convertView.setTag(holder);
        } else {
            Log.d("convertview", "not null");
            holder = (ViewHolder) convertView.getTag();
            RowItem row_pos = rowItems.get(position);
            holder.desc.setText(row_pos.getDesc());
            holder.location.setText(row_pos.getLocation());
            holder.distance.setText(row_pos.getDistance());
        }

        return convertView;
    }

}