package com.greeenwald.myhomies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApiHelper {

//    private static final String BASE_URL = "http://192.168.0.102/";
    private static final String BASE_URL = "http://192.168.56.1/";

    private static String sendPostRequest(JSONObject requestBody, String path) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + path)
                    .openConnection();  // Создание соединения
            connection.setRequestMethod("POST");    // Установка метода запроса
            // Добавление хедера, который говорит о том, что мы отправляем данные в JSON
            connection.setRequestProperty("Content-Type", "application/json");
            // Берем исходящий поток и записываем в него то, что написали выше :)
            connection.getOutputStream().write(requestBody.toString().getBytes());
            // Тут забираем входящий поток, именно в этот момент происходит сам запрос
            InputStream is = connection.getInputStream();

            // Теперь нам нужно считать этот поток и преобразовать его к строке, при помощи сканера
            Scanner scanner = new Scanner(is);

            // Даже если в ответе несколько строк, он будет прочитан, как одна
            scanner.useDelimiter("\\A");

            // Теперь у нас есть ответ, в виде строки.
            String response = scanner.nextLine();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String sendGetRequest(String path, String data) {
        try {
            path += "?" + data;
            HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + path)
                    .openConnection();

            InputStream is = connection.getInputStream();

            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");

            String response = scanner.nextLine();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean doRegister(Context context, String login, String password) {

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("login", login);
            requestBody.put("password", password);

            String response = sendPostRequest(requestBody, "api/register");

            JSONObject jsonResponse = new JSONObject(response);

            if(jsonResponse.has("token")) {
                CacheHelper.saveToken(jsonResponse.getString("token"), context);
                return true;
            }   else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean doLogin(Context context, String login, String password) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("login", login);
            requestBody.put("password", password);

            String response = sendPostRequest(requestBody, "api/login");

            JSONObject jsonResponse = new JSONObject(response);

            if(jsonResponse.has("token")) {
                CacheHelper.saveToken(jsonResponse.getString("token"), context);
                return true;
            }   else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addContact(Context context, String title, String value, String contact_type_id) {
        try {

            String token = CacheHelper.getToken(context);

            JSONObject requestBody = new JSONObject();
            requestBody.put("title", title);
            requestBody.put("value", value);
            requestBody.put("token", token);
            requestBody.put("contact_type_id", contact_type_id);

            String response = sendPostRequest(requestBody, "api/createContact");

            JSONObject jsonResponse = new JSONObject(response);
            if(jsonResponse.has("response"))
                return true;

        }   catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<ContactType> getContactTypes() {
        try {

            List<ContactType> list = new ArrayList<>();

            String response = sendGetRequest("api/getTypes", "");
            // JSONArray, так как в ответе
            // приходит массив объектов
            JSONArray jsonResponse = new JSONArray(response);

            for(int i = 0; i <jsonResponse.length(); i++) { // Перебор всех объектов в массиве
                JSONObject item = jsonResponse.getJSONObject(i);

                String id = item.getString("id");
                String title = item.getString("title");

                ContactType contactType = new ContactType(id, title);
                list.add(contactType);
            }

            return list;

        }   catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Contact> getContacts(Context context) {
        try {

            List<Contact> list = new ArrayList<>();

            String token = CacheHelper.getToken(context);
            String response = sendGetRequest("api/getContacts", "token=" + token);
            Log.d("DEBUG", "r: " + response);
            JSONArray jsonResponse = new JSONArray(response);

            for(int i = 0; i <jsonResponse.length(); i++) {
                JSONObject item = jsonResponse.getJSONObject(i);

                String title = item.getString("title");
                String value = item.getString("value");

                JSONObject jsonType = item.getJSONObject("type");
                String type = jsonType.getString("title");

                Contact contact = new Contact(title, value, type);
                list.add(contact);
            }

            return list;

        }   catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getTypeIcon(String typeTitle) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL
                    + "storage/app/public/icons/" + typeTitle + ".png")
                    .openConnection();

            InputStream is = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        }   catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
