package com.tanuj.electricstats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    TextView tvVoltage;
    TextView tvLevel;
    TextView tvRate;
    TextView vDiff;
    TextView tDiff;
    TextView tvCurrent;
    long lastTime;
    int lastVoltage = -1;
    private final int NEXUS_7_MAH = 4325;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvVoltage = (TextView) findViewById(R.id.tvVoltage);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvRate = (TextView) findViewById(R.id.tvRate);
        vDiff = (TextView) findViewById(R.id.vDiff);
        tDiff = (TextView) findViewById(R.id.tDiff);
        tvCurrent = (TextView) findViewById(R.id.tvCurrent);
        lastTime = System.nanoTime();
        BroadcastReceiver b = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                    update(context, intent);
                }
            }

            private void update(Context context, Intent intent) {
                long newTime = System.nanoTime();
                int newVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                if (lastVoltage != -1) {
                    tDiff.setText(String.valueOf(newTime - lastTime));
                    vDiff.setText(String.valueOf(newVoltage - lastVoltage));
                    double thing = 1000000000*(newVoltage - lastVoltage);
                    double tDifference = newTime - lastTime;
                    double rate = ((double) Math.round(thing*1000 / tDifference))/1000;
                    //Toast.makeText(context, String.valueOf(thing) + " / " + String.valueOf(tDifference), Toast.LENGTH_LONG).show();
                    tvRate.setText(String.valueOf(rate));
                    tvCurrent.setText(String.valueOf(1/(rate)));
                }
                lastVoltage = newVoltage;
                    lastTime = newTime;
                tvVoltage.setText(String.valueOf(newVoltage));
                tvLevel.setText(String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)));
            }
        };
        registerReceiver(b, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
