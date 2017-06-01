package com.alert.AlarmUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;

import com.alert.AppUtils.Applog;

public class MyAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Applog.v("alarm recieved", "*************");
        VibrateDevice(context);
        Intent service1 = new Intent(context, MyAlarmService.class);
        String text = intent.getExtras().getString("note");
        Applog.v("alarm text ", text + "************");

        Bundle b = intent.getExtras();
        service1.putExtras(b);
        context.startService(service1);
    }

    private void VibrateDevice(Context context) {
        try {
            // Vibrate the mobile phone
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
