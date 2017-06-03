package com.dnevnik.lollipop.dnevnik;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dnevnik.lollipop.dnevnik.fragments.DiaryFragment;
import com.dnevnik.lollipop.dnevnik.fragments.FriendsFragment;
import com.dnevnik.lollipop.dnevnik.fragments.HomeworkFragment;
import com.dnevnik.lollipop.dnevnik.fragments.MainPageFragment;
import com.dnevnik.lollipop.dnevnik.fragments.MarksFragment;
import com.dnevnik.lollipop.dnevnik.fragments.MessageFragment;
import com.dnevnik.lollipop.dnevnik.fragments.ProfileFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainPageNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String diaryUrl, marksUrl, homeworkUrl;
    private DiaryFragment diaryFragment;
    private MainPageFragment mainPageFragment;
    private HomeworkFragment homeworkFragment;
    private MarksFragment marksFragment;
    private FriendsFragment friendsFragment;
    private ProfileFragment profileFragment;
    private MessageFragment messageFragment;
    private Fragment previousFragment;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage_navigation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFragments();

        if (fragmentManager == null) fragmentManager = getSupportFragmentManager();

        previousFragment = mainPageFragment;
        if (savedInstanceState == null) {

            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.frame_layout, previousFragment);
            transaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        MainPageContent content = new MainPageContent();
        content.execute();

    }

    private void initFragments() {
        diaryFragment = new DiaryFragment();
        diaryFragment.setToolbar(toolbar);
        mainPageFragment = new MainPageFragment();
        mainPageFragment.setToolbar(toolbar);
        mainPageFragment.setFragmentManager(getSupportFragmentManager());
        marksFragment = new MarksFragment();
        marksFragment.setToolbar(toolbar);
        homeworkFragment = new HomeworkFragment();
        homeworkFragment.setToolbar(toolbar);
        friendsFragment = new FriendsFragment();
        friendsFragment.setToolbar(toolbar);
        profileFragment = new ProfileFragment();
        profileFragment.setToolbar(toolbar);
        messageFragment = new MessageFragment();
        messageFragment.setToolbar(toolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, MainActivity.class);
            CookiesContainer.getInstance().getSharedPreferences().edit().clear().apply();
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        int id = item.getItemId();
        if (id == R.id.nav_main) {
            if (previousFragment != mainPageFragment) {
                transaction.remove(previousFragment);
            }
            else transaction.hide(previousFragment);
            transaction.show(mainPageFragment);
           // transaction.add(R.id.frame_layout, mainPageFragment);

          //  transaction.commit();
            previousFragment = mainPageFragment;
        } else if (id == R.id.nav_diary) {
            if (previousFragment != mainPageFragment) {
                transaction.remove(previousFragment);
            }
            else transaction.hide(previousFragment);
            transaction.add(R.id.frame_layout, diaryFragment);
          //  transaction.commit();
            previousFragment = diaryFragment;
        } else if (id == R.id.nav_marks) {
            if (previousFragment != mainPageFragment) {
                transaction.remove(previousFragment);
            }
            else transaction.hide(previousFragment);
            transaction.add(R.id.frame_layout, marksFragment);
           // transaction.commit();
            previousFragment = marksFragment;

        } else if (id == R.id.nav_homework) {
            if (previousFragment != mainPageFragment) {
                transaction.remove(previousFragment);
            }
            else transaction.hide(previousFragment);
            transaction.add(R.id.frame_layout, homeworkFragment);
            previousFragment = homeworkFragment;

        } /*else if (id == R.id.nav_profile) {
            if (previousFragment != mainPageFragment) {
                transaction.remove(previousFragment);
            }
            else transaction.hide(previousFragment);
            transaction.add(R.id.frame_layout, profileFragment);
            previousFragment = profileFragment;

        } */
        else if (id == R.id.nav_friends) {
            if (previousFragment != mainPageFragment) {
                transaction.remove(previousFragment);
            }
            else transaction.hide(previousFragment);
            transaction.add(R.id.frame_layout, friendsFragment);
            previousFragment = friendsFragment;
        }
        else if (id == R.id.nav_message) {
            if (previousFragment != mainPageFragment) {
                transaction.remove(previousFragment);
            }
            else transaction.hide(previousFragment);
            transaction.add(R.id.frame_layout, messageFragment);
            previousFragment = messageFragment;
        }
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private class MainPageContent extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                try {
                    Document doc = Jsoup.connect("https://dnevnik.ru/user/")
                            .cookies(CookiesContainer.getInstance().getCookies())
                            .get();


                    Elements url = doc.select("div[class=col23 first]")
                            .select("div[class=dashboard]")
                            .attr("style", "float: left; margin-left: 0; width: 193px")
                            .select("div[class=panel blue2]")
                            .select("div[class=rounded]")
                            .select("div[class=cc]")
                            .select("h3[class=icon iBlog]")
                            .select("a");

                    for (Element element : url) {
                        if (element.text().contains("дневник")) {
                            diaryUrl = element.attr("href");
                        } else if (element.text().contains("оценки"))
                            marksUrl = element.attr("href");
                        else if (element.text().contains("домашние задания"))
                            homeworkUrl = element.attr("href");
                    }

                    break;
                } catch (IOException ignored) {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            diaryFragment.setDiaryUrl(diaryUrl);
            marksFragment.setMarksUrl(marksUrl);
            homeworkFragment.setDiaryUrl(homeworkUrl);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(MainPageNavigationActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
