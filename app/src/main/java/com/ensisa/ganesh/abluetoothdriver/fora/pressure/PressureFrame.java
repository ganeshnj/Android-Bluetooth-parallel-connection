package com.ensisa.ganesh.abluetoothdriver.fora.pressure;

/**
 * Created by Ganesh on 6/1/2015.
 */
public enum PressureFrame {
    START ((byte)0x51),
    STOP_CMD ((byte)0xA3),
    STOP_ACK ((byte)0xA5);

    private final byte value;
    private PressureFrame(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
