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
 * Created by lollipop on 16.09.2016.
 */
public class LessonsAdapter extends BaseAdapter{
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Lesson> objects;

    public LessonsAdapter(Context context, ArrayList<Lesson> products) {
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
            view = lInflater.inflate(R.layout.lesson_element, parent, false);
        }

        Lesson p = getLesson(position);

        ((TextView) view.findViewById(R.id.lesson)).setText(p.lesson);
        ((TextView) view.findViewById(R.id.marks)).setText(p.marks);

        return view;
    }
    Lesson getLesson(int position) {
        return ((Lesson) getItem(position));
    }
}
