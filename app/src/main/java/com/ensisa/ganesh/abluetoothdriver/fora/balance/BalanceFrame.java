package com.ensisa.ganesh.abluetoothdriver.fora.balance;

/**
 * Created by Ganesh on 6/3/2015.
 */
public enum BalanceFrame {
    START_PARAMETER ((byte)0x33),
    START_MEASUREMENT ((byte)0x55);

    private final byte value;
    private BalanceFrame(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
