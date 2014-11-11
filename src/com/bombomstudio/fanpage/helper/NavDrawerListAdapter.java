package com.bombomstudio.fanpage.helper;

import java.util.ArrayList;

import com.bombomstudio.fanpage.R;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter{
	private Context context;
    private ArrayList<String> navDrawerItems;
 
    public NavDrawerListAdapter(Context context,
            ArrayList<String> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }
 
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
 
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        txtTitle.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/VN FOCO.OTF"));
        txtTitle.setText(navDrawerItems.get(position));
        return convertView;
    }
}
