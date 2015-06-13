package nl.tue.thermostatv3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by s148494 on 8-6-2015.
 */


public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MySimpleArrayAdapter(Context context, String[] values) {
        super(context, R.layout.item_list_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_list_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);
        // Change the icon for Windows and iPhone
        String s = values[position];
        if (s.startsWith("Home")) {
            imageView.setImageResource(R.drawable.ic_menu);
        } else if (s.startsWith("Week program")) {
            imageView.setImageResource(R.drawable.ic_week_program);
        } else if (s.startsWith("Day/night temperature")) {
            imageView.setImageResource(R.mipmap.ic_daynight);
        } else if (s.startsWith("Settings")) {
            imageView.setImageResource(R.drawable.ic_settings);
        } else {
            imageView.setImageResource(R.drawable.ic_help);
        }

        return rowView;
    }
}