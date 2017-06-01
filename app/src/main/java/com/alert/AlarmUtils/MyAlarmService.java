package com.alert.AlarmUtils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.alert.Activity.MainActivity;
import com.alert.AppUtils.Applog;
import com.alert.R;

public class MyAlarmService extends Service {

    String notificationtext = "";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        try {
            notificationtext = intent.getExtras().getString("note");
            Applog.v("text at MyAlarmService", "****" + notificationtext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (notificationtext.length() != 0) {

            final NotificationCompat.Builder mBulider;
            final NotificationManager notificationManager;
            notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);

            PendingIntent contentIntent;
            Intent notificationIntent = new Intent(this, MainActivity.class);

            contentIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            mBulider = new NotificationCompat.Builder(this);
            mBulider.setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(notificationtext)
                    .setSmallIcon(R.mipmap.app_icon);
            mBulider.setAutoCancel(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Bitmap icon = BitmapFactory.decodeResource(
                        getResources(), R.mipmap.app_icon);
                mBulider.setLargeIcon(icon);
                mBulider.setSmallIcon(R.mipmap.app_icon);
            } else {
                mBulider.setSmallIcon(R.mipmap.app_icon);
            }
            mBulider.setContentIntent(contentIntent);
            notificationManager
                    .notify(1, mBulider.build());

        }
    }

}
