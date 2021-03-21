package com.greeenwald.myhomies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText et_login;
    EditText et_password;

    class Login extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return ApiHelper.doLogin(LoginActivity.this, strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                Intent intent = new Intent(LoginActivity.this, ContactsActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);
    }

    public void login(View view) {
        String login = et_login.getText().toString();
        String password = et_password.getText().toString();
        new Login().execute(login, password);
    }

    public void register(View view) {
        // Так как переход на экран авторизации происходит из экрана регистрации, достаточно
        // просто завершить активность авторизации и мы вернемся на экран регистрации
        finish();
    }
}