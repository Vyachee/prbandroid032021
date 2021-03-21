package com.greeenwald.myhomies;

public class Contact {
    String title;
    String value;
    String type;

    public Contact(String title, String value, String type) {
        this.title = title;
        this.value = value;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }
}

