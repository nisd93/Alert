package com.alert.database;


import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import com.alert.Alarm;

import java.util.UUID;


/**
 * This class wraps cursor to add a function which gets alarm instance
 */
public class AlarmCursorWrapper extends CursorWrapper {

    public AlarmCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Alarm getAlarm() {

        // Get all fields from DB

        String uuidString = getString(getColumnIndex(AlarmTable.Columns.UUID));
        String date = getString(getColumnIndex(AlarmTable.Columns.TIME));
        String title = getString(getColumnIndex(AlarmTable.Columns.TITLE));
        int timeHour = getInt(getColumnIndex(AlarmTable.Columns.HOUR));
        int timeMinute = getInt(getColumnIndex(AlarmTable.Columns.MINUTE));
        // Create a new alarm

        Alarm alarm = new Alarm(UUID.fromString(uuidString));
        alarm.setTimeHour(timeHour);
        alarm.setTimeMinute(timeMinute);
//        Alarm alarm = new Alarm();

        alarm.setDate(date);
        Log.e("Alarm Date",""+alarm.getDate());
        alarm.setTitle(title);

        return alarm;
    }
}
