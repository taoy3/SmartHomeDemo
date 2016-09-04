package com.connection.ui;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.connection.R;

import java.util.List;

/**
 * Created by taoy3 on 16/9/4.
 */
public class WifiInfoAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private int layoutId;
    private List<ScanResult> list;
    public WifiInfoAdapter(Context context, int layoutId, List<ScanResult> mifiList){
        this.inflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.list=mifiList;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(layoutId,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ScanResult scanResult = list.get(position);
        viewHolder.textView.setText(scanResult.SSID+"\t"+Math.abs(scanResult.level)+"%");
        return convertView;
    }
    private static class ViewHolder{
        private TextView textView;
        private ViewHolder(View view){
            textView = (TextView) view.findViewById(R.id.wifi_name);
        }
    }
}
