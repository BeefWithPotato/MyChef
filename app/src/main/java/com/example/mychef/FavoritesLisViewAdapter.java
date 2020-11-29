package com.example.mychef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoritesLisViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Recipe> recipes;
    public FavoritesLisViewAdapter(Context context, ArrayList<Recipe> recipes){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView TitleText, authorText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.favorites_list_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.Grid_img);
            holder.TitleText = (TextView) convertView.findViewById(R.id.Title);
            holder.authorText = (TextView) convertView.findViewById(R.id.username);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.TitleText.setText("Egg");
        holder.authorText.setText("Miranda");
        return convertView;
    }
}
