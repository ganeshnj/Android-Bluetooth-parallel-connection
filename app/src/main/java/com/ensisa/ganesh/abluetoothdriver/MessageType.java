package com.ensisa.ganesh.abluetoothdriver;

/**
 * Created by Ganesh on 5/28/2015.
 */
public enum MessageType {
    MESSAGE_STATE_NONE (0),
    MESSAGE_STATE_LISTEN (1),
    MESSAGE_STATE_CONNECTING (2),
    MESSAGE_STATE_CONNECTED (3),
    MESSAGE_CONNECTION_FAILED (4),
    MESSAGE_CONNECTION_LOST (5),
    MESSAGE_READ (6),
    MESSAGE_WRITE (7),
    MESSAGE_ERROR (8),
    MESSAGE_TOAST (9),
    MESSAGE_STATE_CHANGE (9);

    private final int value;
    private MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
