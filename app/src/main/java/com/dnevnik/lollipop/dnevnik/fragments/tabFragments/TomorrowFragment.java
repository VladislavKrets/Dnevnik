package com.dnevnik.lollipop.dnevnik.fragments.tabFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
 * Created by lollipop on 11.09.2016.
 */
public class TomorrowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private ListView drawerListView;
    private ArrayList<String> content = new ArrayList<>();
    private View view;
    private ArrayAdapter arrayAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tomorrow, null);
        view = v;
        drawerListView = (ListView) v.findViewById(R.id.tomorrow_listview);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.tomorrow_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        content.clear();
        if (drawerListView.getAdapter() != null) {
            ((ArrayAdapter<String>)drawerListView.getAdapter()).clear();
        }
        new SheduleFiller().execute();
        return v;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        content.clear();
        if (drawerListView.getAdapter() != null) {
            ((ArrayAdapter<String>)drawerListView.getAdapter()).clear();
        }
        new SheduleFiller().execute();
    }

    private class SheduleFiller extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                while (true) {
                    Document doc = Jsoup.connect("https://dnevnik.ru/user/")
                            .cookies(CookiesContainer.getInstance().getCookies())
                            .get();
                    String tomorrowUrl = doc.select(".col23.first")
                            .select(".panel.blue2")
                            .select(".rounded")
                            .select(".cc")
                            .select(".col50").last()
                            .select(".bullets")
                            .select("li").select("a").attr("href");
                    String date = doc.select(".col23.first")
                            .select(".panel.blue2")
                            .select(".rounded")
                            .select(".cc")
                            .select(".col50").last()
                            .select("p").text();
                    content.add(date);

                    try {
                        System.out.println(tomorrowUrl);
                        Document docToday = Jsoup.connect(tomorrowUrl)
                                .cookies(CookiesContainer.getInstance().getCookies())
                                .get();
                        Elements elements = docToday.select(".col34.first")
                                .select(".rounded")
                                .select(".cc")
                                .select("tbody")
                                .select("tr");

                        for (Element element : elements) {
                            String lessonNumber = element.select(".nowrap")
                                    .select(".s3").text();
                            String lesson = element.select("td")
                                    .select(".s2.strong")
                                    .select("a").text();
                            content.add(lessonNumber + " " + lesson);
                        }
                    } catch (IllegalArgumentException e) {
                        content.add("Нет уроков");
                    }

                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            arrayAdapter = new ArrayAdapter<>(view.getContext(),
                    R.layout.listview_element, R.id.textView, content);
            drawerListView.setAdapter(arrayAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }
}
