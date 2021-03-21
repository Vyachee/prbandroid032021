package com.greeenwald.myhomies;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    List<ContactType> contactTypeList;
    List<String> contactTypeStringList;
    RecyclerView rv_contacts;

    class GetContactTypes extends AsyncTask<Void, Void, List<ContactType>> {

        @Override
        protected List<ContactType> doInBackground(Void... voids) {
            return ApiHelper.getContactTypes();
        }

        @Override
        protected void onPostExecute(List<ContactType> contactTypes) {
            super.onPostExecute(contactTypes);
            contactTypeList = contactTypes;
            contactTypeStringList = new ArrayList<>();
            for(ContactType ct: contactTypeList)
                contactTypeStringList.add(ct.getTitle());
        }
    }

    class CreateContact extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            return ApiHelper.addContact(ContactsActivity.this, strings[0], strings[1], strings[2]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                Toast.makeText(
                        ContactsActivity.this,
                        "Контакт успешно создан",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetContacts extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            return ApiHelper.getContacts(ContactsActivity.this);
        }

        @Override
        protected void onPostExecute(List<Contact> list) {
            super.onPostExecute(list);
            ContactsAdapter adapter = new ContactsAdapter(list);
            LinearLayoutManager manager = new LinearLayoutManager(ContactsActivity.this);
            rv_contacts.setAdapter(adapter);
            rv_contacts.setLayoutManager(manager);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        rv_contacts = findViewById(R.id.rv_contacts);
        new GetContactTypes().execute();
        new GetContacts().execute();
    }

    public void addContact(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ContactsActivity.this);
        builder.setTitle("Создание нового контакта");

        // LayoutInflater - класс, который из layout-файла может создать View-элемент
        LayoutInflater inflater = LayoutInflater.from(ContactsActivity.this);
        View v = inflater.inflate(R.layout.add_contact_dialog, null, false);

        Spinner s_type = v.findViewById(R.id.s_type);
        EditText et_title = v.findViewById(R.id.et_title);
        EditText et_value = v.findViewById(R.id.et_value);
        if(contactTypeStringList == null) return;
        String[] array = contactTypeStringList.toArray(new String[contactTypeStringList.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ContactsActivity.this,
                android.R.layout.simple_spinner_item,
                array
        );
        s_type.setAdapter(adapter);

        builder.setView(v);
        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Получаем индекс выбранного элемента
                int selectedTypeIndex = s_type.getSelectedItemPosition();
                // Берем из списка тип контакта по индексу
                ContactType contactType = contactTypeList.get(selectedTypeIndex);

                String title = et_title.getText().toString();
                String value = et_value.getText().toString();
                // Здесь берем id того типа элемента, который  выбран
                String contact_type_id = contactType.getId();

                Contact contact = new Contact(title, value, contactType.title);
                ((ContactsAdapter) rv_contacts.getAdapter()).addContact(contact);

                new CreateContact().execute(title, value, contact_type_id);
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}