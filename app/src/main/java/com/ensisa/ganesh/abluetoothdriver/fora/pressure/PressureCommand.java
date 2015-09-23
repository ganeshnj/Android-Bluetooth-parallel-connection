package com.ensisa.ganesh.abluetoothdriver.fora.pressure;

/**
 * Created by Ganesh on 6/1/2015.
 */
public class PressureCommand {
    private byte[] mFrame;
    private boolean mReply;

    public PressureCommand(PressureMessageID id) {
        if(id == PressureMessageID.TURN_OFF)
            mReply = false;
        else
            mReply = true;

        this.mFrame = PressureWriter.createFrame(id);
    }

    public PressureCommand(PressureMessageID id, int user) {
        if(id == PressureMessageID.TURN_OFF)
            mReply = false;
        else
            mReply = true;

        this.mFrame = PressureWriter.createFrame(id, user);
    }

    public PressureCommand(PressureMessageID id, int index, int user) {
        if(id == PressureMessageID.TURN_OFF)
            mReply = false;
        else
            mReply = true;

        this.mFrame = PressureWriter.createFrame(id, index, user);
    }

    public byte[] getFrame() {
        return mFrame;
    }

    public boolean isReply() {
        return mReply;
    }

    public PressureMessageID getMessageID(){
        byte cmd = mFrame[1];
        if(cmd == PressureMessageID.READ_CLOCK.getValue()){
            return PressureMessageID.READ_CLOCK;
        }
        else if(cmd == PressureMessageID.READ_MODEL.getValue()){
            return PressureMessageID.READ_MODEL;
        }
        else if(cmd == PressureMessageID.READ_DATA_1.getValue()){
            return PressureMessageID.READ_DATA_1;
        }
        else if(cmd == PressureMessageID.READ_DATA_2.getValue()){
            return PressureMessageID.READ_DATA_2;
        }
        else if(cmd == PressureMessageID.READ_SERIAL_1.getValue()){
            return PressureMessageID.READ_SERIAL_1;
        }
        else if(cmd == PressureMessageID.READ_SERIAL_2.getValue()){
            return PressureMessageID.READ_SERIAL_2;
        }
        else if(cmd == PressureMessageID.READ_COUNT.getValue()){
            return PressureMessageID.READ_COUNT;
        }
        else if(cmd == PressureMessageID.WRITE_CLOCK.getValue()){
            return PressureMessageID.WRITE_CLOCK;
        }
        else if(cmd == PressureMessageID.TURN_OFF.getValue()){
            return PressureMessageID.TURN_OFF;
        }
        else
            return PressureMessageID.INIT;
    }

}
