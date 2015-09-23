package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

/**
 * Created by Ganesh on 5/30/2015.
 */
public enum TemperatureFrame {
    START ((byte)0x51),
    STOP_CMD ((byte)0xA3),
    STOP_ACK ((byte)0xA5);

    private final byte value;
    private TemperatureFrame(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
