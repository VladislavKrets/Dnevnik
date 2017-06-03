package com.dnevnik.lollipop.dnevnik.fragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dnevnik.lollipop.dnevnik.CookiesContainer;
import com.dnevnik.lollipop.dnevnik.R;

import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MarksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private String marksUrl;
    private Toolbar toolbar;
    private ArrayList<String> lessons = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private View view;
    private String marks;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_marks, null);
        view = v;
        listView = (ListView) v.findViewById(R.id.marks_listview);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.marks_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        lessons.clear();
        if (arrayAdapter != null) arrayAdapter.clear();
        toolbar.setTitle("Оценки");
        new FillLessons().execute();
        return v;
    }

    public void setMarksUrl(String marksUrl) {
        this.marksUrl = marksUrl;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        lessons.clear();
        if (arrayAdapter != null) arrayAdapter.clear();
        new FillLessons().execute();
    }


    private class FillLessons extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                MarksFragment.this.marks = "Нет оценок";
                Document doc = Jsoup.connect(marksUrl)
                        .cookies(CookiesContainer.getInstance().getCookies())
                        .get();
                Elements elements = doc.select("table[id=journal]")
                        .select("tbody")
                        .select("tr");
                String lesson;
                StringBuilder builder = new StringBuilder("");

                for (Element element : elements) {
                    lesson = element.select(".s2")
                            .select("a")
                            .select(".u").text();
                    if (lesson != null && !lesson.isEmpty()) {

                        Elements marks = element.select("td[style=text-align:left;]")
                                .select("a");
                        for (Element markElement : marks) {
                            System.out.println(markElement.text());
                            if (builder.length() != 0 && builder.length() + 1 % 20 == 0) builder.append("\n");
                            else
                            builder.append(markElement.text()).append(" ");
                        }
                        if (builder != null && !builder.toString().isEmpty()) {
                            builder.deleteCharAt(builder.length() - 1);
                            MarksFragment.this.marks = builder.toString();
                        }
                        lessons.add(lesson + ":\n" + MarksFragment.this.marks);
                    }
                    MarksFragment.this.marks = "Нет оценок";
                    builder = new StringBuilder("");
                }
                if (lessons.isEmpty()) lessons.add("Нет оценок");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            arrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.listview_element,
                    R.id.textView, lessons);
            listView.setAdapter(arrayAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}

