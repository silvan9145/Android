package com.davidsilvan.fileconcealer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by David on 3/25/2016.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    private Context context;
    private List<Item> itemList;

    public ItemAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        this.context = context;
        itemList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.row, parent, false);

        final Item item = itemList.get(position);
        TextView itemText = (TextView) view.findViewById(R.id.itemText);
        itemText.setText(item.getString());

        return view;
    }
}
