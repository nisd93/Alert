package com.alert;

import android.content.Context;

import com.alert.AlarmUtils.AlarmScheduler;
import com.alert.database.AlarmDBService;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by kena on 5/10/2017.
 */

public class Alarm {

    String title;
    String date;
    private int     timeHour;
    private int     timeMinute;

    private UUID id;

    private AlarmDBService alarmDBService;

    public Alarm()
    {
        this(UUID.randomUUID());
    }

    public int getTimeHour() {
        return timeHour;
    }

    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
    }

    public int getTimeMinute() {
        return timeMinute;
    }

    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
    }

    public Alarm(UUID alarmId) {

        alarmDBService = AlarmDBService.getInstance(MyApp.getAppContext());

        id = alarmId;

        Calendar calendar = Calendar.getInstance();

        timeHour = calendar.get(Calendar.HOUR_OF_DAY);
        timeMinute = calendar.get(Calendar.MINUTE);

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UUID getId() {
        return id;
    }

    /**
     * Schedules the alarm via the AlarmScheduler
     * @return alarm time
     */
    public long schedule() {

        Context context = MyApp.getAppContext();

//        if(isEnabled) {
//            AlarmScheduler.cancelAlarm(context, this);
//        } else {
//            isEnabled = true;
//        }

        alarmDBService.updateAlarm(this);

        return AlarmScheduler.scheduleAlarm(context, this);
    }
    /**
     * Cancel
     */
    public void cancel() {

        Context context = MyApp.getAppContext();

//        setEnabled(false);
        AlarmScheduler.cancelAlarm(context, this);

        alarmDBService.updateAlarm(this);
    }
    public void setId(UUID id) {
        this.id = id;
    }
}
