package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

import com.ensisa.ganesh.abluetoothdriver.BluetoothService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Ganesh on 5/30/2015.
 */
public class TemperatureSession {
    public List<TemperatureCommand> lastCommands;
    private BluetoothService mBluetoothService;
    private String mDeviceAddress;

    public TemperatureSession() {
        this.lastCommands = new ArrayList<TemperatureCommand>();
    }

    public TemperatureSession(BluetoothService bluetoothService, String deviceAddress) {
        this.mBluetoothService = bluetoothService;
        this.mDeviceAddress = deviceAddress;
        this.lastCommands = new ArrayList<TemperatureCommand>();
    }

    public void init(){
        TemperatureCommand command = new TemperatureCommand(TemperatureMessageID.INIT);
        mBluetoothService.write(command.getFrame(), mDeviceAddress);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        send(command.getMessageID());
        //mBluetoothService.write(command.getFrame(), mDeviceAddress);
    }

    public void send() {
        if(!lastCommands.isEmpty()){
            TemperatureCommand command = lastCommands.get(0);
            mBluetoothService.write(command.getFrame(), mDeviceAddress);
        }
    }

    public void send(TemperatureMessageID messageID){
        TemperatureCommand command = new TemperatureCommand(messageID);
        if(lastCommands.isEmpty()){
            mBluetoothService.write(command.getFrame(), mDeviceAddress);
            lastCommands.add(command);
        } else{
            lastCommands.add(command);
        }
    }

    public void send(TemperatureMessageID messageID, int index){
        TemperatureCommand command = new TemperatureCommand(messageID, index);
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
        if (buffer[0] != TemperatureFrame.START.getValue()) return false;
        if (buffer[6] != TemperatureFrame.STOP_ACK.getValue()) return false;
        if (buffer[7] != TemperatureConstant.checksum(buffer, 0, 7)) return false;

        if(!lastCommands.isEmpty()){
            if(buffer[1] == lastCommands.get(0).getFrame()[1]){
                lastCommands.remove(0);
                return true;
            }
        }
        return true;
    }
}