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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.dnevnik.lollipop.dnevnik.CookiesContainer;
import com.dnevnik.lollipop.dnevnik.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by lollipop on 14.09.2016.
 */
public class ProfileFragment extends Fragment{
    private ImageView profileImageView;
    private TextView profileName;
    private EditText multiLineText;
    private ListView wallUserView;
    private ArrayList<String> content = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private String avatarUrl;
    private String name;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        profileImageView = (ImageView) view.findViewById(R.id.profile_imageView);
        profileName = (TextView) view.findViewById(R.id.profile_name);
        multiLineText = (EditText) view.findViewById(R.id.multiLineText);
        wallUserView = (ListView) view.findViewById(R.id.wall_user_view);
        toolbar.setTitle("Профиль");
        new FillContent().execute();
        return view;
    }
    private class FillContent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup.connect("https://dnevnik.ru/user/user.aspx")
                        .cookies(CookiesContainer.getInstance().getCookies())
                        .get();
                Elements elements = doc.select(".page-wrapper")
                        .select("div[id=content]")
                        .select(".profile");

                avatarUrl = elements
                        .select("a")
                        .select("img")
                        .attr("src");
                System.out.println(avatarUrl);
                name = elements.select("h2")
                        .select("a").text();
               /* Elements infoElements = elements.select("dl");

                for (Element element : infoElements) {

                }*/

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                profileImageView.setImageBitmap(BitmapFactory.decodeStream(new URL(avatarUrl).openStream()));
                profileName.setText(name);
            } catch (IOException e) {
                System.out.println(avatarUrl);
                e.printStackTrace();
            }
        }
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
