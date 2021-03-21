package com.greeenwald.myhomies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RegisterActivity extends AppCompatActivity {

    EditText et_login;
    EditText et_password;

    class Register extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return ApiHelper.doRegister(RegisterActivity.this, strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                // Регистрация успешна
                Intent intent = new Intent(RegisterActivity.this, ContactsActivity.class);
                Toast.makeText(
                        RegisterActivity.this,
                        "Регистрация успешна",
                        Toast.LENGTH_SHORT)
                        .show();
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);
    }

    public void register(View view) {
        String login = et_login.getText().toString();
        String password = et_password.getText().toString();
        new Register().execute(login, password);
    }

    public void login(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}