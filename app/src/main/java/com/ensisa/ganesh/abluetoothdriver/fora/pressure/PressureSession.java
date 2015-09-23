package com.ensisa.ganesh.abluetoothdriver.fora.pressure;

import com.ensisa.ganesh.abluetoothdriver.BluetoothService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ganesh on 6/1/2015.
 */
public class PressureSession {
    public List<PressureCommand> lastCommands;
    private BluetoothService mBluetoothService;
    private String mDeviceAddress;

    public PressureSession() {
        this.lastCommands = new ArrayList<PressureCommand>();
    }

    public PressureSession(BluetoothService bluetoothService, String deviceAddress) {
        this.mBluetoothService = bluetoothService;
        this.mDeviceAddress = deviceAddress;
        this.lastCommands = new ArrayList<PressureCommand>();
    }

    public void init(){
        PressureCommand command = new PressureCommand(PressureMessageID.INIT);
        mBluetoothService.write(command.getFrame(), mDeviceAddress);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        send(command.getMessageID());
        //mBluetoothService.write(command.getFrame(), mDeviceAddress);
    }

    public void send() {
        if(!lastCommands.isEmpty()){
            PressureCommand command = lastCommands.get(0);
            mBluetoothService.write(command.getFrame(), mDeviceAddress);
        }
    }

    public void send(PressureMessageID messageID){
        PressureCommand command = new PressureCommand(messageID);
        if(lastCommands.isEmpty()){
            mBluetoothService.write(command.getFrame(), mDeviceAddress);
            lastCommands.add(command);
        } else{
            lastCommands.add(command);
        }
    }

    public void send(PressureMessageID messageID, int user){
        PressureCommand command = new PressureCommand(messageID, user);
        if(lastCommands.isEmpty()){
            mBluetoothService.write(command.getFrame(), mDeviceAddress);
            lastCommands.add(command);
        } else{
            lastCommands.add(command);
        }
    }

    public void send(PressureMessageID messageID, int index, int user){
        PressureCommand command = new PressureCommand(messageID, index, user);
        if(lastCommands.isEmpty()){
            mBluetoothService.write(command.getFrame(), mDeviceAddress);
            lastCommands.add(command);
        } else{
            lastCommands.add(command);
        }
    }

    public byte checksum (byte buffer[], int from, int to) {
        byte control = 0;
        for (int i=from; i<to;++i) {
            control+=buffer[i];
        }
        return control;
    }

    public boolean checkSanity(byte[] buffer, int length) {
        if (length != 8) return false;
        if (buffer[0] != PressureFrame.START.getValue()) return false;
        if (buffer[6] != PressureFrame.STOP_ACK.getValue()) return false;
        if (buffer[7] != PressureConstant.checksum(buffer, 0, 7)) return false;

        if(!lastCommands.isEmpty()){
            if(buffer[1] == lastCommands.get(0).getFrame()[1]){
                lastCommands.remove(0);
                return true;
            }
        }
        return true;
    }
}
