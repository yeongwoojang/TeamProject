package com.example.yourschedule.OBJECT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleDTO {
    private String date;
    private List<String> schedule;
    private List<Boolean> isComplete;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public List<String> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<String> schedule) {
        this.schedule = schedule;
    }

    public List<Boolean> getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(List<Boolean> isComplete) {
        this.isComplete = isComplete;
    }


}
