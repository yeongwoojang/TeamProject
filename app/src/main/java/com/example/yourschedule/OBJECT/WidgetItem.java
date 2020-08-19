package com.example.yourschedule.OBJECT;

public class WidgetItem {
    boolean _id;
    String content;

    public WidgetItem(boolean _id, String content) {
        this._id = _id;
        this.content = content;
    }

    public boolean get_id() {
        return _id;
    }

    public void set_id(boolean _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}