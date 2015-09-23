package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

import android.os.Bundle;

import com.ensisa.ganesh.abluetoothdriver.fora.pressure.PressureCommand;

/**
 * Created by Ganesh on 5/31/2015.
 */
public class TemperatureWriter {

    public static byte[] createFrame(TemperatureMessageID messageID) {
        if(messageID.getValue() == TemperatureMessageID.READ_CLOCK.getValue()){
            return createGetClock(messageID);
        }
        else if(messageID.getValue() == TemperatureMessageID.READ_MODEL.getValue()){
            return createGetModel(messageID);
        }
        else if(messageID.getValue() == TemperatureMessageID.READ_SERIAL_1.getValue()){
            return createGetSerial1(messageID);
        }
        else if(messageID.getValue() == TemperatureMessageID.READ_SERIAL_2.getValue()){
            return createGetSerial2(messageID);
        }
        else if(messageID.getValue() == TemperatureMessageID.READ_COUNT.getValue()){
            return createGetCount(messageID);
        }
        else if(messageID.getValue() == TemperatureMessageID.WRITE_CLOCK.getValue()){
            return createSetClock(messageID);
        }
        else if(messageID.getValue() == TemperatureMessageID.START_TEMPERATURE.getValue()){
            return createStartTemperature(messageID);
        }
        else if(messageID.getValue() == TemperatureMessageID.TURN_OFF.getValue()){
            return createTurnOff(messageID);
        }
        else if(messageID.getValue() == TemperatureMessageID.CLEAR.getValue()){
            return createClear(messageID);
        }
        return null;
    }

    public static byte[] createFrame(TemperatureMessageID messageID, int index){
        if(messageID.getValue() == TemperatureMessageID.READ_DATA_1.getValue()){
            return createGetData1(messageID, index);
        }
        else if(messageID.getValue() == TemperatureMessageID.READ_DATA_2.getValue()){
            return createGetData2(messageID, index);
        }
        return null;
    }

    private static byte[] createGetClock (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte[] createGetModel (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static  byte [] createGetData1 (TemperatureMessageID messageID, int index) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) index;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static  byte [] createGetData2 (TemperatureMessageID messageID, int index) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) index;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createGetSerial1 (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createGetSerial2 (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createGetCount (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createSetClock (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createStartTemperature (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createTurnOff (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createClear (TemperatureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = TemperatureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = TemperatureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }
}
