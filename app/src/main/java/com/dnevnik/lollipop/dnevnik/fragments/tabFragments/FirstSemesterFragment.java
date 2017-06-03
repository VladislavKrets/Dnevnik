package com.dnevnik.lollipop.dnevnik.fragments.tabFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dnevnik.lollipop.dnevnik.R;

/**
 * Created by lollipop on 16.09.2016.
 */
public class FirstSemesterFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first_semester, null);
        return v;
    }
}
