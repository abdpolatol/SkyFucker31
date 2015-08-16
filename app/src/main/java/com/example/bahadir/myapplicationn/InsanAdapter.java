package com.example.bahadir.myapplicationn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InsanAdapter extends ArrayAdapter<Insan> {

    Context context;
    int layoutresourceid;
    Insan[] data = null;

    public InsanAdapter(Context context, int resource, Insan[] data) {
        super(context, resource, data);
        layoutresourceid = resource;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        InsanHolder holder = null;
        if(row==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(layoutresourceid , parent , false);
            holder = new InsanHolder();
            holder.image1= (ImageView) row.findViewById(R.id.imgIcon);
            holder.tv1 = (TextView) row.findViewById(R.id.txtTitle);
            row.setTag(holder);
        }
        else{
            holder = (InsanHolder) row.getTag();
        }
        Insan insan = data[position];
        holder.tv1.setText(insan.title);
        holder.image1.setImageResource(insan.icon);
        return row;
    }

    static class InsanHolder{
        ImageView image1;
        TextView tv1;
    }
}
