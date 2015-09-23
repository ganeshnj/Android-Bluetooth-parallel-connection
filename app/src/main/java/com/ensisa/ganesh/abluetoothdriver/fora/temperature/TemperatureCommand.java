package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

/**
 * Created by Ganesh on 5/31/2015.
 */
public class TemperatureCommand {

    private byte[] mFrame;
    private boolean mReply;

    public TemperatureCommand(TemperatureMessageID id) {
        if(id == TemperatureMessageID.TURN_OFF)
            mReply = false;
        else
            mReply = true;

        this.mFrame = TemperatureWriter.createFrame(id);
    }

    public TemperatureCommand(TemperatureMessageID id, int index) {
        if(id == TemperatureMessageID.TURN_OFF)
            mReply = false;
        else
            mReply = true;

        this.mFrame = TemperatureWriter.createFrame(id, index);
    }

    public byte[] getFrame() {
        return mFrame;
    }

    public boolean isReply() {
        return mReply;
    }

    public TemperatureMessageID getMessageID(){
        byte cmd = mFrame[1];
        if(cmd == TemperatureMessageID.READ_CLOCK.getValue()){
            return TemperatureMessageID.READ_CLOCK;
        }
        else if(cmd == TemperatureMessageID.READ_MODEL.getValue()){
            return TemperatureMessageID.READ_MODEL;
        }
        else if(cmd == TemperatureMessageID.READ_DATA_1.getValue()){
            return TemperatureMessageID.READ_DATA_1;
        }
        else if(cmd == TemperatureMessageID.READ_DATA_2.getValue()){
            return TemperatureMessageID.READ_DATA_2;
        }
        else if(cmd == TemperatureMessageID.READ_SERIAL_1.getValue()){
            return TemperatureMessageID.READ_SERIAL_1;
        }
        else if(cmd == TemperatureMessageID.READ_SERIAL_2.getValue()){
            return TemperatureMessageID.READ_SERIAL_2;
        }
        else if(cmd == TemperatureMessageID.READ_COUNT.getValue()){
            return TemperatureMessageID.READ_COUNT;
        }
        else if(cmd == TemperatureMessageID.WRITE_CLOCK.getValue()){
            return TemperatureMessageID.WRITE_CLOCK;
        }
        else if(cmd == TemperatureMessageID.START_TEMPERATURE.getValue()){
            return TemperatureMessageID.START_TEMPERATURE;
        }
        else if(cmd == TemperatureMessageID.TURN_OFF.getValue()){
            return TemperatureMessageID.TURN_OFF;
        }
        else
            return TemperatureMessageID.INIT;
    }
}
