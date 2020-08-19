package com.example.yourschedule.OBJECT;

public class Schdule {
    private String item;
    private boolean isChk = false;


    public Schdule(String item) {
        this.item = item;

    }

    public Schdule(String item, boolean isChk) {
        this.item = item;
        this.isChk = isChk;

    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean isChk() {
        return isChk;
    }

    public void setChk(boolean chk) {
        isChk = chk;
    }



}
