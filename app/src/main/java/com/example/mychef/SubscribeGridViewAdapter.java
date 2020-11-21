package com.example.mychef;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SubscribeGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    ArrayList<Recipe> recipes;
    public SubscribeGridViewAdapter(Context context, ArrayList<Recipe> recipes){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
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
        public TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.layout_grid_item, null);
            holder = new SubscribeGridViewAdapter.ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.Grid_img);
            holder.textView = (TextView)convertView.findViewById(R.id.Title);
            convertView.setTag(holder);
        } else{
            holder = (SubscribeGridViewAdapter.ViewHolder) convertView.getTag();
        }
        holder.textView.setText(recipes.get(position).getRecipeName());
        Glide.with(holder.imageView.getContext()).load(recipes.get(position).getCoverImage()).into(holder.imageView);
        return convertView;
    }
}
