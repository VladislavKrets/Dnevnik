package com.dnevnik.lollipop.dnevnik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private EditText loginEditText, passwordEditText;
    private Button loginButton;
    private String login;
    private String password;
    private TextView textView;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_layout);
        loginEditText = (EditText) findViewById(R.id.editTextLogin);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.errorTextView);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.contains("login") && sharedPreferences.contains("password")){
            swipeRefreshLayout.setOnRefreshListener(this);
            onClick(null);
        }
        else {
            loginEditText.setVisibility(EditText.VISIBLE);
            passwordEditText.setVisibility(EditText.VISIBLE);
            loginButton.setVisibility(Button.VISIBLE);
            loginButton.setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View v) {
        loginButton.setEnabled(false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            login = sharedPreferences.getString("login", "");
            password = sharedPreferences.getString("password", "");
            if (login.isEmpty() || password.isEmpty()) throw new NullPointerException();
        } catch (NullPointerException e) {
            login = loginEditText.getText().toString();
            password = passwordEditText.getText().toString();
        }

        runOnUiThread(new Update());
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        runOnUiThread(new Update());
    }
    private class Update implements Runnable{

        @Override
        public void run() {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Connection.Response res = Jsoup.connect("https://login.dnevnik.ru/login")
                        .data("exceededAttempts", "False",
                                "ReturnUrl", "",
                                "login", login,
                                "password", password,
                                "Captcha.Input", "",
                                "Captcha.Id", "aacaa40a-5d8a-4378-bbae-b0ce35d2478d")
                        .method(Connection.Method.POST)
                        .execute();
                if (res.url().toString().equals("https://login.dnevnik.ru/login")) {

                    textView.setText("Неверное имя пользователя или пароль");
                    loginButton.setEnabled(true);
                } else {
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                    SharedPreferences.Editor ed = sharedPreferences.edit();
                    ed.clear();
                    ed.putString("login", login);
                    ed.putString("password", password);
                    ed.apply();
                    CookiesContainer.getInstance().setSharedPreferences(sharedPreferences);
                    Intent intent = new Intent(MainActivity.this, MainPageNavigationActivity.class);
                    CookiesContainer.getInstance().setCookies(res.cookies());
                    startActivity(intent);
                    finish();
                    swipeRefreshLayout.setRefreshing(false);
                }
            } catch (IOException ignored) {
                loginButton.setEnabled(true);
                textView.setText("Проверьте интернет соединение");
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
