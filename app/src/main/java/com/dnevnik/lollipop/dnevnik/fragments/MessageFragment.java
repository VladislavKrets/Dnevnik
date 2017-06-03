package com.dnevnik.lollipop.dnevnik.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.dnevnik.lollipop.dnevnik.CookiesContainer;
import com.dnevnik.lollipop.dnevnik.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lollipop on 01.10.2016.
 */

public class MessageFragment extends Fragment{
    private View view;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> content = new ArrayList<>();
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        listView = (ListView) view.findViewById(R.id.message_list_view);
        this.view = view;
        content.clear();
        if (arrayAdapter != null) arrayAdapter.clear();
        toolbar.setTitle("Сообщения");
        new ContentLoader().execute();

        return view;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    private class ContentLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://messenger.dnevnik.ru/")
                        .cookies(CookiesContainer.getInstance().getCookies())
                        .get();
                Elements elements = doc.select(".col23.first")
                        .select(".messages2.grid")
                        .select("tbody")
                        .select("tr");
                for (int i = 1; i < elements.size(); i++) {
                    String name = elements.get(i)
                            .select(".tdName")
                            .select("a").text();
                    System.out.println(name);
                    content.add(name);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            arrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.listview_element,
                    R.id.textView, content);
            listView.setAdapter(arrayAdapter);
        }
    }

}
