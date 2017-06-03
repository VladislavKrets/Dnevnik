package com.dnevnik.lollipop.dnevnik;

import android.content.SharedPreferences;
import android.util.Property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lollipop on 04.09.2016.
 */
public class CookiesContainer {
    private static CookiesContainer instance;
    private Map<String, String> cookies;
    private SharedPreferences sharedPreferences;
    private CookiesContainer(){}
    public static synchronized CookiesContainer getInstance() {
        if (instance == null) {
            instance = new CookiesContainer();
        }
        return instance;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;

    }

    public Map<String, String> getCookies() {

        return cookies;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
