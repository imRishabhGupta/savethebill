package com.example.user.savethebill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 30-03-2016.
 */
public class BillAdapter extends BaseAdapter {

    List<Bill> bills;
    Context mContext;
    public BillAdapter(Context context,List<Bill> bills1){
        super();
        mContext=context;
        bills=bills1;
        System.out.println("Reached "+bills1.get(0).getOwner_name());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.list_item, null);

        Bill bill=bills.get(position);
        TextView textView=(TextView)convertView.findViewById(R.id.text);
        textView.setText(bill.getBill_Type()+"  "+bill.getOwner_name());


        return convertView;
    }

    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
}
