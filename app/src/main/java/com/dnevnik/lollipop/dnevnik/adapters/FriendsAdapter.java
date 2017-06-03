package com.dnevnik.lollipop.dnevnik.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dnevnik.lollipop.dnevnik.R;

import java.util.ArrayList;

/**
 * Created by lollipop on 13.09.2016.
 */
public class FriendsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Friend> objects;

    public FriendsAdapter(Context context, ArrayList<Friend> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.friends_fragment_element, parent, false);
        }

        Friend p = getFriend(position);

        ((TextView) view.findViewById(R.id.textViewName)).setText(p.name);
        ((ImageView) view.findViewById(R.id.avatar_image)).setImageBitmap(p.bitmap);

        return view;
    }
    Friend getFriend(int position) {
        return ((Friend) getItem(position));
    }
    public void clear() {
        objects.clear();
    }

}
