package com.example.user.savethebill;

/**
 * Created by rohanpc on 4/24/2016.
 */

        import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> bills;

    public CustomListAdapter(Activity activity, List<Movie> movieItems) {
        this.activity = activity;
        this.bills = movieItems;
    }

    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public Object getItem(int location) {
        return bills.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);


        ImageView thumbNail = (ImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView bill_name = (TextView) convertView.findViewById(R.id.title);
        //TextView owner = (TextView) convertView.findViewById(R.id.rating);
        TextView type = (TextView) convertView.findViewById(R.id.genre);
       // TextView lastdate = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        Movie m = bills.get(position);
        if (m.getImagestring() != null) {

        byte[] imageAsBytes = Base64.decode(m.getImagestring().getBytes(), Base64.DEFAULT);
        thumbNail.setImageBitmap(
                BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
        );}

        bill_name.setText(m.getBillName());
       // owner.setText(m.getNameofowner());
        type.setText(m.getType());
        //lastdate.setText(m.getGuarantee());


        return convertView;
    }

}