package com.ensisa.ganesh.abluetoothdriver.common.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by Ganesh on 6/4/2015.
 */
public class BluetoothFragment extends Fragment {
    public static final String ARG_NAME = "device_name";
    public static final String ARG_ADDRESS = "device_address";

    protected String mDeviceName;
    protected String mDeviceAddress;

    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.mDeviceAddress = deviceAddress;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
    }
}
