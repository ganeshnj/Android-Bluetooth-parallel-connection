package com.ensisa.ganesh.abluetoothdriver;

/**
 * Created by Ganesh on 5/22/2015.
 */
public class Constants {

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String BUFFER = "buffer";
    public static final String TOAST = "toast";

    static public byte checksum (byte buffer[], int from, int to) {
        byte control = 0;
        for (int i=from; i<to;++i) {
            control+=buffer[i];
        }
        return control;
    }
}
