package com.jerbi.halim.mytaskofsecurity.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.jerbi.halim.mytaskofsecurity.service.CountDownService;
import com.jerbi.halim.mytaskofsecurity.activity.AlertActivity;
import com.jerbi.halim.mytaskofsecurity.activity.MainActivity;
import com.jerbi.halim.mytaskofsecurity.interfaces.securityInterface;

/**
 * Created by Lou_g on 29/04/2018.
 */

public class CheckReciever extends BroadcastReceiver implements securityInterface {
    final long DAY = 86400000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "BROAD service",
                Toast.LENGTH_LONG).show();
        Intent intentAlert;

        if(this.securityCheck())
        {
            Intent intentService = new Intent(context.getApplicationContext(), CountDownService.class);
            intentService.addCategory(CountDownService.MY_SERVICE);
            context.stopService(intentService);


            intentAlert = new Intent(context,MainActivity.class);
             context.startActivity(intentAlert);

            SharedPreferences preferences  = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor  editor= preferences.edit();

            editor.putLong("time",DAY);
            editor.apply();


            context.startService(intentService);

        }
        else
            {
                Intent intentService = new Intent(context.getApplicationContext(), CountDownService.class);
                intentService.addCategory(CountDownService.MY_SERVICE);
                context.stopService(intentService);

             intentAlert = new Intent(context,AlertActivity.class);
                context.startActivity(intentAlert);


                SharedPreferences preferences  = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor  editor= preferences.edit();

                editor.putLong("time",DAY);
                editor.apply();


                context.startService(intentService);

            }


    }

    @Override
    public boolean securityCheck() {

          return Math.random() < 0.5;

    }
}
