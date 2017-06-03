package com.dnevnik.lollipop.dnevnik.fragments;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by lollipop on 10.09.2016.
 */
public class DiaryFragment extends Fragment {
    private String diaryUrl;
    private ArrayList<String> contentList = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private View view;
    private Toolbar toolbar;

    public DiaryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_diary, null);

        toolbar.setTitle("Дневник");

        listView = (ListView) v.findViewById(R.id.listView1);
        contentList.clear();
        if (listView.getAdapter() != null) {
            ((ArrayAdapter<String>) listView.getAdapter()).clear();
        }
        view = v;
        new AsyncThread().execute();
        return v;
    }

    public void setDiaryUrl(String diaryUrl) {
        this.diaryUrl = diaryUrl;
    }
    private class AsyncThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                try {

                    Document doc = Jsoup.connect(diaryUrl)
                            .cookies(CookiesContainer.getInstance().getCookies())
                            .get();

                    Elements elements = doc.select("div[id=diarydaysleft]").select(".col24.first").select(".col24");
                    int count = 0;
                    for (Element element : elements) {
                        LinkedHashSet<String> list = new LinkedHashSet<>();
                        list.add(element.getAllElements().select("h3").last().text());
                        Elements e = element.select("tbody")
                                .select("tr");
                        for (Element elementA : e) {
                            String lessonNumber = elementA
                                    .select("td")
                                    .select(".s2").select(".light").text();
                            String str = elementA
                                    .select("td")
                                    .select(".s2").select(".strong").last().attr("title");
                            str = Parser.unescapeEntities(str, false);
                            if (str != null && !str.isEmpty() && !str.matches("\\p{Punct}+")) {
                                list.add(lessonNumber + " " + str);
                            }
                        }
                        if (count == 0) {
                            count++;
                            list.clear();
                            continue;
                        }
                        if (list.size() == 1) list.add("Нет уроков");
                        list.add(" ");
                        contentList.addAll(list);
                        list.clear();

                    }
                    elements = doc.select("div[id=diarydaysright]").select(".col24.first");

                    for (Element element : elements) {
                        LinkedHashSet<String> list = new LinkedHashSet<>();
                        list.add(element.getAllElements().select("h3").text());
                        Elements e = element.select("tbody")
                                .select("tr");
                        for (Element elementA : e) {
                            String lessonNumber = elementA
                                    .select("td")
                                    .select(".s2").select(".light").text();

                            String str = elementA
                                    .select("td")
                                    .select(".s2").select(".strong").last().attr("title");
                            str = Parser.unescapeEntities(str, false);
                            if (str != null && !str.isEmpty() && !str.matches("\\p{Punct}+")) {
                                list.add(lessonNumber + " " + str);
                            }
                        }
                        if (list.size() == 1) list.add("Нет уроков");
                        list.add(" ");
                        contentList.addAll(list);
                        list.clear();

                    }

                    break;
                } catch (IOException ignored) {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ArrayAdapter<String>(view.getContext(), R.layout.listview_element,
                    R.id.textView, contentList);


            listView.setAdapter(adapter);
        }
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
