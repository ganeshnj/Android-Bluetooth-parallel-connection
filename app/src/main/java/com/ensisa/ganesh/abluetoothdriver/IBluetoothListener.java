package com.ensisa.ganesh.abluetoothdriver;

import android.os.Bundle;
import android.os.Message;

/**
 * Created by Ganesh on 5/28/2015.
 */
public interface IBluetoothListener {
    public void onWrite(Message msg, int length, byte[] buffer);
    public void onRead(Message msg, int length, byte[] buffer);
    public void onLost(Message msg);
    public void onFailed(Message msg);
    public void onConnected(Message msg, Bundle bundle);
    public void onConnecting(Message msg, Bundle bundle);
    public void onListen(Message msg);
    public void onNone(Message msg);
    public void onError(Message msg);
}
