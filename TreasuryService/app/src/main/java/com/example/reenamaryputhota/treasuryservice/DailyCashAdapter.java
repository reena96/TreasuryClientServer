package com.example.reenamaryputhota.treasuryservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.reenamaryputhota.common.DailyCash;

import java.util.List;

/**
 * Created by reenamaryputhota on 5/1/18.
 */

public class DailyCashAdapter  extends ArrayAdapter<DailyCash> {

    private List<DailyCash> dailyCashes;
    Context mContext;
    int i = 0;

    // View lookup cache
    private static class ViewHolder {
        TextView id, year, month, day, dayOfWeek, dayOpenAmt, dayCloseAmt;
    }



    public DailyCashAdapter(List<DailyCash> dailyCashes, Context context) {
        super(context, R.layout.list_item, dailyCashes);
        this.dailyCashes = dailyCashes;
        this.mContext=context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        DailyCash dailyCash = dailyCashes.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.id = convertView.findViewById(R.id._id);
            viewHolder.year =  convertView.findViewById(R.id.year);
            viewHolder.month = convertView.findViewById(R.id.month);
            viewHolder.day =  convertView.findViewById(R.id.day);
            viewHolder.dayOfWeek = convertView.findViewById(R.id.dayOfWeek);
            viewHolder.dayOpenAmt = convertView.findViewById(R.id.dayOpenAmt);
            viewHolder.dayCloseAmt = convertView.findViewById(R.id.dayCloseAmt);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (dailyCashes.size() <= 0) {
            viewHolder.id.setText("No Data");

        }
        else {
            // Populate the data from the data object via the viewHolder object
            // into the template view.
            viewHolder.id.setText((position+1)+"");
            viewHolder.year.setText(dailyCash.getYear()+"");
            viewHolder.month.setText(dailyCash.getMonth()+"");
            viewHolder.day.setText(dailyCash.getDay()+"");
            viewHolder.dayOfWeek.setText(dailyCash.getDayOfWeek()+"");
            viewHolder.dayOpenAmt.setText(dailyCash.getDayOpenAmt()+"");
            viewHolder.dayCloseAmt.setText(dailyCash.getDayCloseAmt()+"");
        }
        // Return the completed view to render on screen
        return convertView;
    }


}
