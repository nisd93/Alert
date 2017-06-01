package com.alert.AlarmUtils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.alert.Activity.MainActivity;
import com.alert.R;

import java.util.UUID;


public class AlarmNotificationManager {

    private static final String TAG = "AlarmNotificationMgr";

    private static AlarmNotificationManager sManager;

    private Context mContext;
    private UUID mCurrentAlarmId;
    private long mCurrentAlarmTime;
    private boolean mNotificationsActive;

    private AlarmNotificationManager(Context context) {
        mContext = context;
        resetState();
    }

    public static AlarmNotificationManager getInstance(Context context) {
        if (sManager == null) {
            sManager = new AlarmNotificationManager(context);
        }
        return sManager;
    }

    public static Notification createAlarmNotification(Context context, UUID alarmId) {

        Intent showUnlockIntent = new Intent(context, MainActivity.class);
        showUnlockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        showUnlockIntent.putExtra(AlarmRingingService.ALARM_ID, alarmId);

        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                (int) Math.abs(alarmId.getLeastSignificantBits()),
                showUnlockIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alert")
                .setContentText("Check")
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setContentIntent(contentIntent);

        return builder.build();
    }

    public void handleAlarmRunningNotificationStatus(UUID alarmId) {
        updateStateWithAlarmDetails(alarmId, 0, false);
        AlarmRingingService.startForegroundService(
                mContext,
                alarmId,
                0);

    }

    public void disableNotifications() {
        // We only attempt to disable the notification if it is already active
        if (mNotificationsActive) {
            AlarmRingingService.stopForegroundService(mContext);
            resetState();
        }
    }

    private void updateStateWithAlarmDetails(UUID alarmId, long alarmTime, boolean wakelockEnable) {
        mNotificationsActive = true;
        mCurrentAlarmId = alarmId;
        mCurrentAlarmTime = alarmTime;
    }

    private boolean doesCurrentStateMatchAlarmDetails(UUID alarmId, long alarmTime, boolean wakelockEnable) {
        return (mCurrentAlarmTime == alarmTime &&
                mCurrentAlarmId.equals(alarmId));
    }

    private void resetState() {
        mNotificationsActive = false;
        mCurrentAlarmId = new UUID(0, 0);
        mCurrentAlarmTime = 0;
    }

}
