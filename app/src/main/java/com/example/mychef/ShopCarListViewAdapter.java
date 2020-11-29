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

public class ShopCarListViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    ArrayList<String> ingredients = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<String > imgs = new ArrayList<String>();

    ShopCarListViewAdapter(Context context){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

        ingredients.add("Tea");
        price.add("$10.8");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/mychef-9ea56.appspot.com/o/images%2FRecipeImages%2Ftea.jpg?alt=media&token=348cea7d-359c-49d3-9eeb-3f6addcdd3fd");
        ingredients.add("Sugar");
        price.add("$3.64");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/mychef-9ea56.appspot.com/o/images%2FRecipeImages%2Fsugar.jpg?alt=media&token=8807b1df-4c63-4f38-a247-b34ba0ea5c58");
        ingredients.add("flour");
        price.add("$7.99");
        imgs.add("https://firebasestorage.googleapis.com/v0/b/mychef-9ea56.appspot.com/o/images%2FRecipeImages%2Fflour.jpg?alt=media&token=fe22387b-f9ce-4128-a853-47e3134ddebe");
    }

    @Override
    public int getCount() {
        return ingredients.size();
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
        public TextView tvTitle, tvPrice;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.shop_car_list_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.Ingredient_imageView);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.Ingredient_textView);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.price_textView);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText(ingredients.get(position));
        holder.tvPrice.setText(price.get(position));
        Glide.with(holder.imageView.getContext()).load(imgs.get(position)).into(holder.imageView);

        return convertView;
    }
}
