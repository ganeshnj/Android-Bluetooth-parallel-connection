package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

/**
 * Created by Ganesh on 5/30/2015.
 */
public enum TemperatureMessageID {
    READ_CLOCK ((byte)0x23),
    READ_MODEL ((byte)0x24),
    READ_DATA_1 ((byte)0x25),
    READ_DATA_2 ((byte)0x26),
    READ_SERIAL_1 ((byte)0x27),
    READ_SERIAL_2 ((byte)0x28),
    READ_COUNT ((byte)0x2B),
    WRITE_CLOCK ((byte)0x33),
    START_TEMPERATURE ((byte)0x41),
    TURN_OFF ((byte)0x50),
    CLEAR ((byte) 0x52),
    INIT ((byte) 0x24);

    private final byte value;
    private TemperatureMessageID(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}

