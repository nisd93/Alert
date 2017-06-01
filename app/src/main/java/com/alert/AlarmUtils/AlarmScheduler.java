package com.alert.AlarmUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.alert.Alarm;
import com.alert.R;

import java.util.Calendar;


public class AlarmScheduler {

    // Key used in intent for alarm ID
    public static final String X_ALARM_ID = "x_alarm_id";
    public static final String x_alarm_time = "x_alarm_time";
    public static long scheduleAlarm(Context context, Alarm alarm) {

        PendingIntent pendingIntent = createPendingIntent(context, alarm);

        Calendar calenderNow = Calendar.getInstance();
        long time = getAlarmTime(calenderNow,alarm);

        setAlarm(context, time, pendingIntent);

        return time;
    }

    /**
     * Get alarm time
     * @param alarm alarm to get time
     * @return alarm time
     */
    public static long getAlarmTime(Calendar now,Alarm alarm) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, alarm.getTimeHour());
        cal.set(Calendar.MINUTE,alarm.getTimeMinute());

        long diff = cal.getTime().getTime() - now.getTime().getTime();

        long millis = 0;
//        try {
//            Log.e("to Pasrse",alarm.getDate());
//            SimpleDateFormat dateFormat;
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
//                dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US);
//            } else {
//                dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
//            }
//            Date date = dateFormat.parse(alarm.getDate());
//            millis = date.getTime();

//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Log.e("time log", diff + "");
        return diff;
    }

    /**
     * Set Alarm via AlarmManager
     *
     * Beginning with API 19 (KITKAT) alarm delivery is inexact: the OS will shift
     * alarms in order to minimize wakeups and battery use. There are new APIs to
     * support applications which need strict delivery guarantees
     *
     * @param context
     * @param time
     * @param pendingIntent
     */
    private static void setAlarm(Context context, long time, PendingIntent pendingIntent) {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
        Log.e("Set Alarm","::"+time);
    }

    /**
     * Cancel Alarm via AlarmManager
     * @param context
     * @param alarm alarm instance to cancela
     */
    public static void cancelAlarm(Context context, Alarm alarm) {
        PendingIntent pIntent = createPendingIntent(context, alarm);
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    private static PendingIntent createPendingIntent(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmWakeReceiver.class);
        intent.putExtra(X_ALARM_ID, alarm.getId());
        intent.putExtra(x_alarm_time, alarm.getTitle());
        return PendingIntent
                .getBroadcast(
                        context,
                        (int) Math.abs(alarm.getId().getLeastSignificantBits()),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Create a toast string to show when is the next alarm
     * @param context
     * @param timeInMillis
     * @return
     */
    public static String getTimeToAlarmString(Context context, long timeInMillis, int idTemplate) {
        long delta = timeInMillis - System.currentTimeMillis();
        long hours = delta / (1000 * 60 * 60);
        long minutes = delta / (1000 * 60) % 60;
        long days = hours / 24;
        hours = hours % 24;

        String daySeq = (days == 0) ? "" :
                (days == 1) ? context.getString(R.string.day) :
                        context.getString(R.string.days, Long.toString(days));

        String minSeq = (minutes == 0) ? "" :
                (minutes == 1) ? context.getString(R.string.minute) :
                        context.getString(R.string.minutes, Long.toString(minutes));

        String hourSeq = (hours == 0) ? "" :
                (hours == 1) ? context.getString(R.string.hour) :
                        context.getString(R.string.hours, Long.toString(hours));

        boolean dispDays = days > 0;
        boolean dispHour = hours > 0;
        boolean dispMinute = minutes > 0;

        int index = (dispDays ? 1 : 0) |
                (dispHour ? 2 : 0) |
                (dispMinute ? 4 : 0);

        String[] formats = context.getResources().getStringArray(idTemplate);
        return String.format(formats[index], daySeq, hourSeq, minSeq);
    }

}
