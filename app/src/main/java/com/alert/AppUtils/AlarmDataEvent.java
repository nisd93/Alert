package com.alert.AppUtils;

/**
 * Created by kena on 5/31/2017.
 */

public class AlarmDataEvent {
    private String title;
    private String date;
    private String id;

    public AlarmDataEvent(String title, String date,String id) {
        this.title = title;
        this.date=date;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
