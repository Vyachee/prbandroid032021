package com.greeenwald.myhomies;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.Holder> {

    List<Contact> contactList; // Список элементов, которые нужно отображать

    public ContactsAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
        notifyDataSetChanged();
    }

    class LoadIcon extends AsyncTask<ImageLoaderHelper, Void, ImageLoaderHelper> {

        @Override
        protected ImageLoaderHelper doInBackground(ImageLoaderHelper... imageLoaderHelpers) {
            Bitmap b = ApiHelper.getTypeIcon(imageLoaderHelpers[0].type);
            imageLoaderHelpers[0].bitmap = b;
            return imageLoaderHelpers[0];
        }

        @Override
        protected void onPostExecute(ImageLoaderHelper imageLoaderHelper) {
            super.onPostExecute(imageLoaderHelper);
            if(imageLoaderHelper.bitmap != null)
                imageLoaderHelper.iv_target.setImageBitmap(imageLoaderHelper.bitmap);
        }
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.contact_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ContactsAdapter.Holder holder, int position) {
        // При назначении элемента на позиции position
        // position соответствует индексу элемента в списке, поэтому мы можем взять контакт:
        Contact contact = contactList.get(position);
        // и установить нужные данные в холдере
        holder.tv_title.setText(contact.getTitle());
        holder.tv_value.setText(contact.getValue());

        ImageLoaderHelper helper = new ImageLoaderHelper();
        helper.iv_target = holder.i_icon;
        helper.type = contact.getType();

        new LoadIcon().execute(helper);
    }

    @Override
    public int getItemCount() {
        // При отрисовке, RecyclerView берет здесь кол-во элементов, которое нужно отрисовать
        return contactList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        // Это холдер, предствление элемента списка

        // Объявляем элементы, с которыми будем работать
        ImageView i_icon;
        TextView tv_title;
        TextView tv_value;

        public Holder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            // и здесь инициализируем их
            i_icon = itemView.findViewById(R.id.i_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_value = itemView.findViewById(R.id.tv_value);
        }
    }

}

