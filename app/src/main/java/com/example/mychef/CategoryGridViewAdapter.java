package com.example.mychef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<CategoryPageActivity.Category> categories;
    public CategoryGridViewAdapter(Context context, ArrayList<CategoryPageActivity.Category> categories){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
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
            convertView = mLayoutInflater.inflate(R.layout.category_grid_item, null);
            holder = new CategoryGridViewAdapter.ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.Grid_img);
            holder.textView = (TextView)convertView.findViewById(R.id.Title);
            convertView.setTag(holder);
        } else{
            holder = (CategoryGridViewAdapter.ViewHolder) convertView.getTag();
        }
        holder.textView.setText(categories.get(position).name);
        Glide.with(mContext).load(categories.get(position).url).into(holder.imageView);
        return convertView;
    }
}
