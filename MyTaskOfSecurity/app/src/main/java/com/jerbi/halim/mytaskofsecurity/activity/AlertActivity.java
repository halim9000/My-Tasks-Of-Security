package com.jerbi.halim.mytaskofsecurity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.jerbi.halim.mytaskofsecurity.R;

public class AlertActivity extends Activity {

    boolean response = false;
    private MediaPlayer mp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);


    }

    @Override
    protected void onResume() {
        super.onResume();


        mp = MediaPlayer.create(this, R.raw.alarm);
        mp.start();

        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AlertActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(AlertActivity.this);
        }
        builder.setTitle("Alert")
                .setMessage("Your security system is WEAK ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mp.stop();
                      Intent intent = new Intent(AlertActivity.this,MainActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                        

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show();

        

    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();

    }
}