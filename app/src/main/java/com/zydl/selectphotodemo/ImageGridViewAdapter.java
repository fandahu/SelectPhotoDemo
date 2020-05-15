package com.zydl.selectphotodemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Sch.
 * Date: 2019/7/18
 * description:
 */
public class ImageGridViewAdapter extends BaseAdapter {
    private ArrayList<Bitmap> imgs;
    private Context mContext;

    public ImageGridViewAdapter(ArrayList<Bitmap> imgs, Context context) {
        this.imgs = imgs;
        mContext = context;
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int i) {
        return imgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_photo_grid_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.itemImg = (ImageView) convertView.findViewById(R.id.iv_head);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(imgs.get(i)).into(viewHolder.itemImg);
        return convertView;

    }

    public void notifyDataSetChanged(ArrayList<Bitmap> imgs) {
        this.imgs = imgs;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView itemImg;
    }
}
