package com.dnevnik.lollipop.dnevnik.fragments;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dnevnik.lollipop.dnevnik.CookiesContainer;
import com.dnevnik.lollipop.dnevnik.adapters.Friend;
import com.dnevnik.lollipop.dnevnik.adapters.FriendsAdapter;
import com.dnevnik.lollipop.dnevnik.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lollipop on 13.09.2016.
 */
public class FriendsFragment extends Fragment {
    private Toolbar toolbar;
    private ListView listView;
    private ArrayList<Friend> content = new ArrayList<>();
    private FriendsAdapter friendsAdapter;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_friends, null);
        toolbar.setTitle("Друзья");
        view = v;
        listView = (ListView) v.findViewById(R.id.listView_friends);

        content.clear();

        if (listView.getAdapter() != null) {
            ((FriendsAdapter) listView.getAdapter()).clear();
        }

        new ContentLoader().execute();
        return v;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    private class ContentLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://dnevnik.ru/user/user.aspx?view=friends")
                        .cookies(CookiesContainer.getInstance().getCookies())
                        .get();
                Elements elements = doc.select(".col34")
                        .select("form[id=formPeople]")
                        .select(".people.grid.panel")
                        .select("tbody")
                        .select("tr");
                String url;
                String name;
                for (Element element : elements) {
                   Elements eUser =  element.select(".tdAvatar")
                            .select(".user_ava")
                            .select("a");
                    url = eUser.select("img").attr("src");
                    name = eUser.attr("title");
                    System.out.println(url);
                    System.out.println(name);
                    content.add(new Friend(name, BitmapFactory.decodeStream(new URL(url).openStream())));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println(content);
            friendsAdapter = new FriendsAdapter(view.getContext(), content);
            listView.setAdapter(friendsAdapter);
        }
    }
}
