package com.ensisa.ganesh.abluetoothdriver.common.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ensisa.ganesh.abluetoothdriver.common.logger.Log;
import com.ensisa.ganesh.abluetoothdriver.common.logger.LogWrapper;

/**
 * Created by Ganesh on 5/28/2015.
 */
public class LoggerActivity extends AppCompatActivity {
    public static final String TAG = "LoggerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected  void onStart() {
        super.onStart();
        initializeLogging();
    }

    /** Set up targets to receive log data */
    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }
}
