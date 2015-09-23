package com.ensisa.ganesh.abluetoothdriver.fora.pressure;

import com.ensisa.ganesh.abluetoothdriver.fora.temperature.TemperatureConstant;

/**
 * Created by Ganesh on 6/1/2015.
 */
public class PressureWriter {
    public static byte[] createFrame(PressureMessageID messageID) {
        if(messageID.getValue() == PressureMessageID.READ_CLOCK.getValue()){
            return createGetClock(messageID);
        }
        else if(messageID.getValue() == PressureMessageID.READ_MODEL.getValue()){
            return createGetModel(messageID);
        }
        else if(messageID.getValue() == PressureMessageID.READ_SERIAL_1.getValue()){
            return createGetSerial1(messageID);
        }
        else if(messageID.getValue() == PressureMessageID.READ_SERIAL_2.getValue()){
            return createGetSerial2(messageID);
        }
        else if(messageID.getValue() == PressureMessageID.WRITE_CLOCK.getValue()){
            return createSetClock(messageID);
        }
        else if(messageID.getValue() == PressureMessageID.TURN_OFF.getValue()){
            return createTurnOff(messageID);
        }
        return null;
    }

    public static byte[] createFrame(PressureMessageID messageID, int user){
        if(messageID.getValue() == PressureMessageID.READ_COUNT.getValue()){
            return createGetCount(messageID, user);
        }
        else if(messageID.getValue() == PressureMessageID.CLEAR.getValue()){
            return createClear(messageID, user);
        }
        return null;
    }

    public static byte[] createFrame(PressureMessageID messageID, int index, int user){
        if(messageID.getValue() == PressureMessageID.READ_DATA_1.getValue()){
            return createGetData1(messageID, index, user);
        }
        else if(messageID.getValue() == PressureMessageID.READ_DATA_2.getValue()){
            return createGetData2(messageID, index, user);
        }
        return null;
    }

    private static byte[] createGetClock (PressureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte[] createGetModel (PressureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static  byte [] createGetData1 (PressureMessageID messageID, int index, int user) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) index;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) user;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static  byte [] createGetData2 (PressureMessageID messageID, int index, int user) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) index;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) user;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createGetSerial1 (PressureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createGetSerial2 (PressureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createGetCount (PressureMessageID messageID, int user) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) user;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createSetClock (PressureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createStartTemperature (PressureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createTurnOff (PressureMessageID messageID) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) 0x00;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }

    private static byte [] createClear (PressureMessageID messageID, int user) {
        byte [] buffer = new byte [8];
        buffer[0] = PressureFrame.START.getValue();
        buffer[1] = messageID.getValue();
        buffer[2] = (byte) user;
        buffer[3] = (byte) 0x00;
        buffer[4] = (byte) 0x00;
        buffer[5] = (byte) 0x00;
        buffer[6] = PressureFrame.STOP_CMD.getValue();
        buffer[7] = TemperatureConstant.checksum(buffer, 0, 7);
        return buffer;
    }
}
