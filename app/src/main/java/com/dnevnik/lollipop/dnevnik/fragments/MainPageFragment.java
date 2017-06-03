package com.dnevnik.lollipop.dnevnik.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dnevnik.lollipop.dnevnik.R;
import com.dnevnik.lollipop.dnevnik.adapters.ViewPagerAdapter;
import com.dnevnik.lollipop.dnevnik.fragments.tabFragments.TodayFragment;
import com.dnevnik.lollipop.dnevnik.fragments.tabFragments.TomorrowFragment;


/**
 * Created by lollipop on 11.09.2016.
 */
public class MainPageFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentManager fragmentManager;
    private ViewPagerAdapter adapter;
    private TodayFragment todayFragment;
    private TomorrowFragment tomorrowFragment;
    private FragmentTransaction transaction;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mainpage, null);

        view = v;
        if (toolbar != null)
        toolbar.setTitle("Главная");

        transaction = getFragmentManager().beginTransaction();
        tabLayout = (TabLayout) v.findViewById(R.id.tablayout);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        todayFragment = new TodayFragment();
        tomorrowFragment = new TomorrowFragment();
        adapter = new ViewPagerAdapter(fragmentManager);
        tabLayout.removeAllTabs();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter.addFragment(todayFragment, "Сегодня");
        adapter.addFragment(tomorrowFragment, "Завтра");
        viewPager.setAdapter(adapter);

    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
