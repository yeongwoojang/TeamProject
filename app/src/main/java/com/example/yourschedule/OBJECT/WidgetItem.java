package com.example.yourschedule.OBJECT;

public class WidgetItem {
    int _id;
    String content;

    public WidgetItem(int _id, String content) {
        this._id = _id;
        this.content = content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}