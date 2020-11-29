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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubscribeGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    ArrayList<Recipe> recipes;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private User userInfo;

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
        public ImageView imageView, profile_icon;
        public TextView textView, userName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.layout_grid_item, null);
            holder = new SubscribeGridViewAdapter.ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.Grid_img);
            holder.textView = (TextView)convertView.findViewById(R.id.Title);
            holder.profile_icon = (ImageView) convertView.findViewById(R.id.profile_image);
            holder.userName = (TextView) convertView.findViewById(R.id.username);
            convertView.setTag(holder);
        } else{
            holder = (SubscribeGridViewAdapter.ViewHolder) convertView.getTag();
        }
        holder.textView.setText(recipes.get(position).getRecipeName());
        Glide.with(holder.imageView.getContext()).load(recipes.get(position).getCoverImage()).into(holder.imageView);

        ViewHolder finalHolder = holder;
        ref.child("User").child(recipes.get(position).getAuthorUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(User.class);
                //get username
                finalHolder.userName.setText(userInfo.getUsername());

                //if user has set an icon, then get and set it
                if(userInfo.getUserIcon() != null){
                    Glide.with(finalHolder.profile_icon.getContext()).load(userInfo.getUserIcon()).into(finalHolder.profile_icon);
                }
                //if not use default
                else{
                    finalHolder.profile_icon.setImageResource(R.drawable.usericon);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        return convertView;
    }
}
