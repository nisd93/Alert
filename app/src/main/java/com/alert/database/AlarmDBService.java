package com.alert.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.alert.Alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * This class is service level for alarm. It provides functions to get/update/delete
 * alarm in DB
 */
public class AlarmDBService {

    private static AlarmDBService mAlarmDBService;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private AlarmDBService(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new AlarmDBHelper(mContext).getWritableDatabase();
    }

    public static AlarmDBService getInstance(Context context) {
        if (mAlarmDBService == null) {
            mAlarmDBService = new AlarmDBService(context);
        }
        return mAlarmDBService;
    }


    /**
     * Get all alarms existing in DB
     * @return list of alarms
     */
    public List<Alarm> getAlarms() {

        List<Alarm> alarms = new ArrayList<>();

        AlarmCursorWrapper cursor = new AlarmCursorWrapper(mDatabase.query(
                AlarmTable.NAME,
                null, // gets all columns
                null,
                null,
                null,
                null,
                null
//                AlarmTable.Columns.HOUR + ", " + AlarmTable.Columns.MINUTE
        ));


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            alarms.add(cursor.getAlarm());
            cursor.moveToNext();
        }
        cursor.close();

        return alarms;
    }

    /**
     * Gets an alarm instance from DB via alarm id
     * @param id alarm id
     * @return alarm
     */
    public Alarm getAlarm(UUID id) {

        AlarmCursorWrapper cursor = new AlarmCursorWrapper(mDatabase.query(
                AlarmTable.NAME,
                null, // gets all columns
                AlarmTable.Columns.UUID + " = ?", // cause
                new String[]{id.toString()}, // args
                null,
                null,
                null
//                AlarmTable.Columns.HOUR + ", " + AlarmTable.Columns.MINUTE // orderby
        ));


        try {
            if (cursor.getCount() == 0) {
                return null;
            } else {
                cursor.moveToFirst();
                return cursor.getAlarm();
            }
        } finally {
            cursor.close();
        }

    }

    /**
     * Add a new alarm in DB
     * @param alarm alarm to add
     */
    public void addAlarm(Alarm alarm) {

        ContentValues values = createContentValues(alarm);

        mDatabase.insert(AlarmTable.NAME, null, values);
    }

    /**
     * Update alarm in DB
     * @param alarm alarm to update
     */
    public void updateAlarm(Alarm alarm) {

        ContentValues values = createContentValues(alarm);

        mDatabase.update(AlarmTable.NAME, values,
                AlarmTable.Columns.UUID + " = ?",
                new String[]{alarm.getId().toString()});
    }

    /**
     * Deletes alarm from DB
     * @param alarm alarm instance to remove
     */
    public void deleteAlarm(Alarm alarm) {
        mDatabase.delete(AlarmTable.NAME,
                AlarmTable.Columns.UUID + " = ?",
                new String[] { alarm.getId().toString() });
    }

    /**
     * Transforms alarm instance to content value format
     * @param alarm alarm to transform
     * @return
     */
    private static ContentValues createContentValues(Alarm alarm) {

        ContentValues values = new ContentValues();

        values.put(AlarmTable.Columns.UUID, alarm.getId().toString());
        values.put(AlarmTable.Columns.TITLE, alarm.getTitle());
        values.put(AlarmTable.Columns.TIME, alarm.getDate());
        values.put(AlarmTable.Columns.HOUR, alarm.getTimeHour());
        values.put(AlarmTable.Columns.MINUTE, alarm.getTimeMinute());

        return values;

    }
}
