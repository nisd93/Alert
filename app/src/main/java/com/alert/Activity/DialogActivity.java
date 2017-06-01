package com.alert.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alert.AppUtils.AlarmDataEvent;
import com.alert.AppUtils.Fonts;
import com.alert.MorphUtils.FabTransform;
import com.alert.MorphUtils.MorphTransform;
import com.alert.R;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Activity which looks like a dialog
 */
public class DialogActivity extends AppCompatActivity {

    private static final String EXTRA_TYPE = "type";

    public static final int TYPE_FAB = 1;
    public static final int TYPE_BUTTON = 2;
    SwitchDateTimeDialogFragment dateTimeFragment;

    public static Intent newIntent(Context context, int type) {
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        return intent;
    }

    private EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(DialogActivity.this);
            }
        });

        View container = findViewById(R.id.container);

        //This could probably be better. Basically checks to see if it is a FabTransform. If not,
        //tries out being a MorphTransform
        switch (getIntent().getIntExtra(EXTRA_TYPE, -1)) {
            case TYPE_FAB:
                FabTransform.setup(this, container);
                break;
            case TYPE_BUTTON:
                MorphTransform.setup(this, container, Color.WHITE,
                        getResources().getDimensionPixelSize(R.dimen.dialog_corners));
                break;
        }

        initViews();
    }

    private void initViews()
    {
        final EditText edtTitle = (EditText)findViewById(R.id.edtTitle);
        final EditText edtTime = (EditText) findViewById(R.id.edtTime);
        TextView tv_dialogTitle = (TextView) findViewById(R.id.dialogTitle);
        edtTime.setTypeface(Fonts.setUtahCondensed(DialogActivity.this));
        edtTitle.setTypeface(Fonts.setUtahCondensed(DialogActivity.this));

        tv_dialogTitle.setTypeface(Fonts.setUtahCondensed(DialogActivity.this));
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
        final SimpleDateFormat myDateFormat;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            myDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US);
        } else {
            myDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        }
//        final SimpleDateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.getDefault());
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
        dateTimeFragment.setMinimumDateTime(new GregorianCalendar(year, month, mDay, mHour, mMinute).getTime());
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
        CardView cvAdd = (CardView)findViewById(R.id.cvAdd);

        cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmDataEvent event=null;
                event=new AlarmDataEvent(edtTitle.getText().toString(),edtTime.getText().toString(),"");
                bus.post(event);
                finish();
            }
        });
    }
}
