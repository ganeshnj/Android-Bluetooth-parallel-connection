package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

import java.util.Calendar;

/**
 * Created by Ganesh on 5/29/2015.
 */
public class TemperatureConstant {
    public static final String DEVICE_NAME = "FORA IR21";

    static public byte checksum (byte buffer[], int from, int to) {
        byte control = 0;
        for (int i=from; i<to;++i) {
            control+=buffer[i];
        }
        return control;
    }

    public static final String KEY_DEVICE_MODEL = "device_model";
    public static final String KEY_DEVICE_YEAR = "device_year";
    public static final String KEY_DEVICE_MONTH = "device_month";
    public static final String KEY_DEVICE_DAY = "device_day";
    public static final String KEY_DEVICE_HOUR = "device_hour";
    public static final String KEY_DEVICE_MINUTE = "device_minute";
    public static final String KEY_TYPE = "type";
    public static final String KEY_OBJECT_TEMPERATURE = "object_temperature";
    public static final String KEY_AMBIENT_TEMPERATURE = "ambient_temperature";
    public static final String KEY_SERIAL_1 = "serial_1";
    public static final String KEY_SERIAL_2 = "serial_2";
}
