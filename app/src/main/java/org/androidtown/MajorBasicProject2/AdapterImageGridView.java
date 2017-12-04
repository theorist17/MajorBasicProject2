package org.androidtown.MajorBasicProject2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Hongin Lee on 2017-11-29.
 */

public class AdapterImageGridView extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Bitmap> img = new ArrayList<>();
    LayoutInflater inf;

    public AdapterImageGridView(Context context, int layout) {
        this.context = context;
        this.layout = layout;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return img.get(position);
    }

    public void addItem(Bitmap bmp){
        img.add(bmp);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        iv.setImageBitmap(img.get(position));

        return convertView;
    }
}