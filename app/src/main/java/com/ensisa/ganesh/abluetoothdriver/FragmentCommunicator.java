package com.ensisa.ganesh.abluetoothdriver;

/**
 * Created by Ganesh on 5/29/2015.
 */
public interface FragmentCommunicator {
    public void passDataToFragment(byte[] buffer, int length, String address);
}
