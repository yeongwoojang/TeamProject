package com.example.yourschedule.OBJECT;

public class Schdule {
    private String item;
    private boolean isChk;


    public Schdule(String item) {
        this.item = item;

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
