package com.alert.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alert.Adapters.AlarmAdapter;
import com.alert.Alarm;
import com.alert.AlarmUtils.MyAlarmReceiver;
import com.alert.AppUtils.AlarmDataEvent;
import com.alert.AppUtils.Applog;
import com.alert.AppUtils.Fonts;
import com.alert.MorphUtils.MorphTransform;
import com.alert.R;
import com.alert.database.AlarmDBService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.cvAddAlarm)
    CardView cvAddAlarm;
    @BindView(R.id.lvAlarms)
    RecyclerView lvAlarms;

    AlarmAdapter adapter;

    private EventBus bus = EventBus.getDefault();

    ArrayList<Alarm> alarmArrayList=new ArrayList<>();

    private boolean mAttached;
    private final Handler mHandler = new Handler();

    @Override
    protected void onStart() {
        super.onStart();

            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            registerReceiver(mIntentReceiver, filter, null, mHandler);
    }


    /**
     * Listen to ACTION_TIME_TICK
     * Update time with new updates
     */
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0)
                Log.e("TIMEE","::");

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTypeface();
        initViews();

        // Register as a subscriber
        bus.register(this);

    }

    private void setTypeface() {
        tvDate.setTypeface(Fonts.setUtahCondensed(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Applog.e("Onresume", "::");
    }

    @Override
    protected void onDestroy() {
        // Unregister
        bus.unregister(this);
        unregisterReceiver(mIntentReceiver);
        super.onDestroy();
    }

    Alarm mAlarm;

    private void initViews() {

//        List<Alarm> alarms = AlarmDBService.getInstance(this).getAlarms();
//
//        if (alarms.isEmpty()) {

//        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        lvAlarms.setLayoutManager(linearLayoutManager);
        lvAlarms.setHasFixedSize(true);
//        lvSearch.setItemAnimator(new Slideo);
        adapter = new AlarmAdapter(MainActivity.this,
                alarmArrayList, new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Alarm data) {

            }
        });
        lvAlarms.setAdapter(adapter);

        List<Alarm> alarms = AlarmDBService.getInstance(this).getAlarms();
        alarmArrayList.addAll(alarms);

        if(!alarmArrayList.isEmpty()) {
            for (int i = 0; i < alarmArrayList.size(); i++) {
                if (alarmArrayList.get(i).getDate() == null) {
                    alarmArrayList.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
        }

        mAlarm = new Alarm();

        Log.e("oncreate",":"+mAlarm.getId());
    }

    @OnClick(R.id.cvAddAlarm)
    public void onViewClicked() {
        /*
        Add alarm through this dialog
         */


        AlarmDBService.getInstance(this).addAlarm(mAlarm);

        Applog.e("CLicked",":"+mAlarm.getId().toString());
        Intent intent = DialogActivity.newIntent(MainActivity.this, DialogActivity.TYPE_BUTTON);
        if (Build.VERSION.SDK_INT >= 21) {
            MorphTransform.addExtras(intent,
                    ContextCompat.getColor(MainActivity.this, R.color.white),
                    getResources().getDimensionPixelSize(R.dimen.dialog_corners));
            ActivityOptions options =
                    ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, cvAddAlarm,
                            getString(R.string.transition_morph));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.do_nothing);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AlarmDataEvent event) {
        Toast.makeText(getApplicationContext(), event.getTitle(), Toast.LENGTH_SHORT).show();

//        UUID alarmID = (UUID) event.getId().toString();
        mAlarm = AlarmDBService.getInstance(this).getAlarm(mAlarm.getId());

//        mAlarm=new Alarm();
        mAlarm.setTitle(event.getTitle());
        mAlarm.setDate(event.getDate());
        String[] splitDate=event.getDate().split(" ");
        String[] splitDateTwo=splitDate[1].split(":");
        mAlarm.setTimeHour(Integer.parseInt(splitDateTwo[0].toString()));
        mAlarm.setTimeMinute(Integer.parseInt(splitDateTwo[1].toString()));
        // Update alarm in DB
        //AlarmDBService.getInstance(this).updateAlarm(mAlarm);

        long newTime = mAlarm.schedule();

        Applog.e("newTime","::"+newTime);
        alarmArrayList.add(mAlarm);
        adapter.notifyDataSetChanged();
    }

    public static void setReminderNotification(Activity activity,
                                               long time, String notificationtext, int notificationId) {

        Applog.v("alarm set called", "*************");
//		addReminder(activity);
        Applog.v("time log at apputil", time + "");
        long futureInMillis = SystemClock.elapsedRealtime() + time;
        Applog.v("time log at apputil", futureInMillis + "");
        Intent myIntent = new Intent(activity, MyAlarmReceiver.class);
        Bundle b = new Bundle();
        b.putString("note", notificationtext);
        myIntent.putExtras(b);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, notificationId, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(
                Activity.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
//                nextUpdate, pendingIntent);
    }

//    public long getRemTimemillis(String date) {
//        long millis = 0;
//        try {
//            String toParse = date;
//            Applog.v("to Pasrse", toParse);
//            SimpleDateFormat dateFormat;
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
//                dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
//            } else {
//                dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//            }
//            Date date = dateFormat.parse(toParse);
//            Applog.v("log", toParse + " & " + date);
//            millis = date.getTime();
//            Applog.v("time log", millis + "");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return millis;
//
//    }

}
