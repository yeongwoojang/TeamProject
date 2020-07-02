package com.example.yourschedule.OBJECT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private String email;
    private String date;
    private String schedule;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
