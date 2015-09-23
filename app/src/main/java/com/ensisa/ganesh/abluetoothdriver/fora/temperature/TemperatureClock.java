package com.ensisa.ganesh.abluetoothdriver.fora.temperature;

import java.util.Calendar;

/**
 * Created by Ganesh on 6/2/2015.
 */
public class TemperatureClock {
    int year;
    int month;
    int day;

    int hour;
    int minute;

    private TemperatureType type;

    public enum TemperatureType {
        EAR, FOREHEAD;
    }


    public TemperatureClock(int year, int month, int day, int hour, int minute) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.month = month;
        this.year = year;
    }

    public TemperatureClock(){
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public String getDate(){
        return day + "/" + month + "/" + year;
    }

    public String getTime(){
        return hour + ":" + minute;
    }

    public String getType() {
        if(type == TemperatureType.FOREHEAD)
            return "Forehead";
        else
            return "Ear";
    }

    public void setType(TemperatureType type) {
        this.type = type;
    }

    public static byte getData0(){

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR)%2000;

        String bDay = String.format("%5s", Integer.toBinaryString(day & 0xFF)).replace(' ', '0');
        String bMonth = String.format("%4s", Integer.toBinaryString(month & 0xFF)).replace(' ', '0');
        String bYear = String.format("%7s", Integer.toBinaryString(year & 0xFF)).replace(' ', '0');

        String data_0 = bMonth.substring(1, 4) + bDay;
        String data_1 = bYear + bMonth.charAt(0);

        byte byte_0 = (byte) Integer.parseInt(data_0, 2);
        byte byte_1 = (byte) Integer.parseInt(data_1, 2);

        return byte_0;
    }

    public static byte getData1(){
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR)%2000;

        String bDay = String.format("%5s", Integer.toBinaryString(day & 0xFF)).replace(' ', '0');
        String bMonth = String.format("%4s", Integer.toBinaryString(month & 0xFF)).replace(' ', '0');
        String bYear = String.format("%7s", Integer.toBinaryString(year & 0xFF)).replace(' ', '0');

        String data_0 = bMonth.substring(1, bMonth.length()) + bDay;
        String data_1 = bYear + bMonth.charAt(0);

        byte byte_0 = (byte) Integer.parseInt(data_0, 2);
        byte byte_1 = (byte) Integer.parseInt(data_1, 2);

        return byte_1;
    }

    public static byte getData2(){
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MINUTE);
        int h = c.get(Calendar.HOUR_OF_DAY);
        return (byte) m;
    }

    public static byte getData3(){
        Calendar c = Calendar.getInstance();
        int m = c.get(Calendar.MINUTE);
        int h = c.get(Calendar.HOUR_OF_DAY);
        return (byte) h;
    }
}
