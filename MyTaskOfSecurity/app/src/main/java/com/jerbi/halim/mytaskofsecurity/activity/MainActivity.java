package com.jerbi.halim.mytaskofsecurity.activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jerbi.halim.mytaskofsecurity.broadcast.CheckReciever;
import com.jerbi.halim.mytaskofsecurity.service.CountDownService;
import com.jerbi.halim.mytaskofsecurity.R;


public class MainActivity extends AppCompatActivity {
    private  final long DAY = 86400000;
    private TextView txt_time ;
    private TextView txt_check;
    private TextView txtAdminTime;
    private TimePicker timePicker ;
    private ImageButton imageButton;
    private Button btn_check;
    private int hour;
    private int minute;
    private long time ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_check = (TextView) findViewById(R.id.txt_check);
        txtAdminTime = (TextView) findViewById(R.id.txt_admin);
        btn_check = (Button) findViewById(R.id.btn_check);



    }

    @Override
    protected void onResume() {
        super.onResume();

        System.setProperty("reboot_me", "");
        String prop = (System.getProperty("reboot_me"));

        if(prop.length() == 0)
        {

        }
        else
        {
            txtAdminTime.setText("the time is defined by the admin");
            txt_time.setText(prop);
            imageButton.setEnabled(false);

            time = Long.valueOf(prop);
        }


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        time = preferences.getLong("time", 0);
        if(time==0)
        {
            SharedPreferences.Editor  editor= preferences.edit();

            editor.putLong("time",DAY);
            editor.apply();
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        MainActivity.this);
                final  View pickers=getLayoutInflater().inflate(R.layout.dialog_time,
                        null);
                dialog.setView(pickers);
                dialog.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                timePicker=(TimePicker)pickers.findViewById(R.id.timePicker);
                                hour = timePicker.getCurrentHour();
                                minute = timePicker.getCurrentMinute();
                                txt_time.setText(hour+":"+minute);
                                txt_time.setTextSize(40);
                                txt_check.setText("Are you sure ?");
                                txt_check.setTextSize(18);
                                final Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);

                                 time = calendar.getTimeInMillis();
                                //  edit1.setText();
                            }
                        });
                dialog.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });


                dialog.show();

            }
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CheckReciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);


                Log.e("millis",time+"");
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,time,AlarmManager.INTERVAL_DAY,pendingIntent);

                Intent intentService = new Intent(getApplicationContext(), CountDownService.class);
                intentService.addCategory(CountDownService.MY_SERVICE);
                stopService( new Intent(getApplicationContext(), CountDownService.class));
                startService(intentService);


                finish();
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(MainActivity.this, "Your check time is defined",
                Toast.LENGTH_LONG).show();
    }
}
