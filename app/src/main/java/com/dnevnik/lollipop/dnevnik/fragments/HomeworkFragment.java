package com.dnevnik.lollipop.dnevnik.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dnevnik.lollipop.dnevnik.R;

/**
 * Created by lollipop on 11.09.2016.
 */
public class HomeworkFragment extends Fragment{
    private String homeworkUrl;
    private Toolbar toolbar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_homework, null);
        toolbar.setTitle("Домашние задания");
        return v;
    }

    public void setDiaryUrl(String homeworkUrl) {
        this.homeworkUrl = homeworkUrl;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
