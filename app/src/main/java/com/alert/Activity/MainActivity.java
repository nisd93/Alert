package com.alert.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alert.Adapters.AlarmAdapter;
import com.alert.Alarm;
import com.alert.AppUtils.Fonts;
import com.alert.MorphUtils.MorphTransform;
import com.alert.R;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTypeface();
        initViews();

    }

    private void setTypeface() {
        tvDate.setTypeface(Fonts.setUtahCondensed(getApplicationContext()));
    }

    private void initViews()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        lvAlarms.setLayoutManager(linearLayoutManager);
        lvAlarms.setHasFixedSize(true);
//        lvSearch.setItemAnimator(new Slideo);
        adapter = new AlarmAdapter(MainActivity.this,
                null, new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Alarm data) {

            }
        });
        lvAlarms.setAdapter(adapter);

    }

    SwitchDateTimeDialogFragment dateTimeFragment;

    private void addAlarm() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.set_alarm_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(mView);
        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        final EditText edtTitle = (EditText) mView.findViewById(R.id.edtTitle);
        final EditText edtTime = (EditText) mView.findViewById(R.id.edtTime);
        TextView tv_dialogTitle = (TextView) mView.findViewById(R.id.dialogTitle);
        edtTime.setTypeface(Fonts.setUtahCondensed(MainActivity.this));
        edtTitle.setTypeface(Fonts.setUtahCondensed(MainActivity.this));

        tv_dialogTitle.setTypeface(Fonts.setUtahCondensed(MainActivity.this));
        final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Utils.hideSoftKeyboard(MainActivity.this);
//                showDateTimePicker();
                dateTimeFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
            }
        });

        final String TAG = "Sample";
        // Construct SwitchDateTimePicker
        dateTimeFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if (dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker)
            );
        }
        // Assign values we want
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        dateTimeFragment.startAtCalendarView();
        dateTimeFragment.set24HoursMode(false);
        Calendar cc = Calendar.getInstance();
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH);
        int mDay = cc.get(Calendar.DAY_OF_MONTH);
        int mHour = cc.get(Calendar.HOUR_OF_DAY);
        int mMinute = cc.get(Calendar.MINUTE);
        dateTimeFragment.setDefaultDateTime(new GregorianCalendar(year, month, mDay, mHour, mMinute).getTime());
        // Define new day and month format
        try {
            dateTimeFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("MMMM dd", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e(TAG, e.getMessage());
        }

        // Set listener for date
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                edtTime.setText(myDateFormat.format(date));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
//                edtDate.setText("");
            }
        });

        CardView cvAdd = (CardView) mView.findViewById(R.id.cvAdd);

        cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @OnClick(R.id.cvAddAlarm)
    public void onViewClicked() {
        /*
        Add alarm through this dialog
         */
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
}
