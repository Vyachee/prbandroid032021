package com.greeenwald.myhomies;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheHelper {


    public static void saveToken(String token, Context context) {

        // SharedPreferences - класс, отвечающий за хранение данных в виде ключ:значение на ус-ве
        // userdata - название таблицы. Если её нет, она создается
        SharedPreferences preferences = context
                .getSharedPreferences("userdata", Context.MODE_PRIVATE);

        // Создаем редактор
        SharedPreferences.Editor editor = preferences.edit();

        // Вносим значение
        editor.putString("token", token);

        // И сохраняем
        editor.apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences = context.
                getSharedPreferences("userdata", Context.MODE_PRIVATE);

                // Берем значение по ключу, его мы задавали в saveToken.
                // Если значение пустое, вернется null
        return preferences.getString("token", null);
    }



}
