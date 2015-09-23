package com.ensisa.ganesh.abluetoothdriver;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Ganesh on 5/28/2015.
 */
public class BluetoothHandler extends Handler {

    IBluetoothListener mBluetoothListener;
    Context mContext;

    public BluetoothHandler() {
        this.mBluetoothListener = null;
        this.mContext = null;
    }

    public BluetoothHandler(IBluetoothListener mBluetoothListener, Context mContext) {
        this.mBluetoothListener = mBluetoothListener;
        this.mContext = mContext;
    }

    public void setBluetoothListener(IBluetoothListener mBluetoothListener) {
        this.mBluetoothListener = mBluetoothListener;
    }

    @Override
    public void handleMessage(Message msg) {
        if (mBluetoothListener == null) return;
        switch (MessageType.values()[msg.what]) {
            case MESSAGE_STATE_NONE: onStateNone(msg); break;
            case MESSAGE_STATE_LISTEN: onStateListen(msg); break;
            case MESSAGE_STATE_CONNECTING: onStateConnecting(msg); break;
            case MESSAGE_STATE_CONNECTED: onStateConnected(msg); break;
            case MESSAGE_CONNECTION_FAILED: onConnectionFailed(msg); break;
            case MESSAGE_CONNECTION_LOST:onConnectionLost(msg); break;
            case MESSAGE_READ: onMessageRead(msg); break;
            case MESSAGE_WRITE: onMessageWrite(msg); break;
            case MESSAGE_ERROR: onMessageError(msg); break;
        }
    }

    private void onStateNone(Message msg) {
        if(mBluetoothListener == null)
            return;
        mBluetoothListener.onNone(msg);
    }

    private void onStateListen(Message msg) {
        if(mBluetoothListener == null)
            return;
        mBluetoothListener.onListen(msg);
    }

    private void onStateConnecting(Message msg) {
        if(mBluetoothListener == null)
            return;
        mBluetoothListener.onConnecting(msg, (Bundle) msg.getData());
    }

    private void onStateConnected(Message msg) {
        if(mBluetoothListener == null)
            return;
        mBluetoothListener.onConnected(msg, (Bundle) msg.getData());
    }

    private void onConnectionFailed(Message msg) {
        if(mBluetoothListener == null)
            return;
        mBluetoothListener.onFailed(msg);
    }

    private void onConnectionLost(Message msg) {
        if(mBluetoothListener == null)
            return;
        mBluetoothListener.onLost(msg);
    }

    private void onMessageRead(Message msg) {
        if(mBluetoothListener == null)
            return;

        int length = msg.arg1;
        byte[] buffer = new byte[length];
        for(int i=0; i<length; i++) {
            buffer[i] = msg.getData().getByteArray(Constants.BUFFER)[i];
        }
        mBluetoothListener.onRead(msg, msg.arg1, buffer);
    }

    private void onMessageWrite(Message msg) {
        if(mBluetoothListener == null)
            return;
        mBluetoothListener.onWrite(msg, msg.arg1, msg.getData().getByteArray(Constants.BUFFER));
    }

    private void onMessageError(Message msg) {
        if(mBluetoothListener == null)
            return;
        mBluetoothListener.onError(msg);
    }
}
